package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import transmission.ServerUpdate;
import entities.EntityPlayer;
import enums.EnumColor;


public class TerraeRasa 
{
	//http://stackoverflow.com/questions/2914375/getting-file-path-in-java
	private static String basePath = "/home/alec/terraerasaserver";
	public volatile static boolean done = false;
	private final static String VERSION = "Alpha 0.1.4";	
	private ServerSettings settings;
	private Vector<ServerConnectionThread> connections = new Vector<ServerConnectionThread>();
	public GameEngine gameEngine;
	public volatile static TerraeRasa terraeRasa;
	private GameEngineThread gameEngineThread;
	private ServerSocket serverSocket;
	private ConsoleInputThread consoleInputThread;
	private static Object gameEngineLock = new Object();
	
	public static void main(String[] args)
	{
		terraeRasa = new TerraeRasa();
		terraeRasa.run();
	}
	
	public void run()
	{
		Log.log("Terrae Rasa Server " + VERSION);
		Log.log("Edit the server.properties file for advanced configurations.");
		settings = SettingsIO.loadSettings();	

		this.gameEngine = new GameEngine(settings.universeName);
		gameEngineThread = new GameEngineThread(gameEngine);
		gameEngineThread.start();

		consoleInputThread = new ConsoleInputThread();
		consoleInputThread.start();

		try {
			serverSocket = new ServerSocket(settings.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(!done)
		{
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(!done) 
			{
				try {
					handleSocketConnection(socket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//Issue cleanup from the game engine thread, this one is locked.
	}
	
	private void handleSocketConnection(Socket socket) throws IOException
	{
		ObjectOutputStream os = null;
		ObjectInputStream is = null;
		try {
			 os = new ObjectOutputStream(socket.getOutputStream());
			 is = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String val = is.readUTF();
		
		Log.log("[" + socket.getInetAddress() + "] : " + val);
		
		if(val.equals("/serverinfo"))
		{
			os.writeObject(new String[] { VERSION, connections.size() + "/" + settings.maxPlayers + " Players", settings.serverMessage, "You connected with: " + socket.getInetAddress() });
			os.flush();
		}
		else if(val.equals("/connect"))
		{
			String[] message = { "" };
			boolean allowed = SecurityManager.verifyConnectionIsAllowed(settings, socket, message);
			Log.log("[" + socket.getInetAddress() + "] : " + message[0]);
			
			if(allowed && connections.size() < settings.maxPlayers)
			{
				os.writeUTF("connection accepted");
				os.flush();
				Log.log("[" + socket.getInetAddress() + "] : " + "connection accepted");
				registerGameConnectionThread(socket, os, is);
			}
			else if(connections.size() >= settings.maxPlayers)
			{
				Log.log("[" + socket.getInetAddress() + "] : " + "connection denied: too many players");
				os.writeUTF("connection denied : too many players.");
				os.flush();
			}
			else
			{
				Log.log("[" + socket.getInetAddress() + "] : " + "connection denied");
				os.writeUTF("connection not allowed");
				os.flush();
			}
		}		
	}
	
	private synchronized void registerGameConnectionThread(Socket socket, ObjectOutputStream os, ObjectInputStream is)
	{
		ServerConnectionThread thread = new ServerConnectionThread(new WorldLock(gameEngine), socket, os, is);
		addConnection(thread);
		thread.start();	
	}
	
	public synchronized void addConnection(ServerConnectionThread thread)
	{
		connections.add(thread);
	}
			
	/**
	 * Gets the game version for the current launch
	 * @return the game version for the current launch
	 */
	public final static String getVersion()
	{
		return VERSION;
	}

	public final static String getBasePath() {
		return basePath;
	}
	
	public synchronized Vector<ServerConnectionThread> getConnections()
	{
		return connections;
	}
	
	public synchronized void close()
	{
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Iterator<ServerConnectionThread> it = terraeRasa.getConnections().iterator();
		while(it.hasNext())
		{
			ServerConnectionThread thread = it.next();
			thread.close();
		}	
		
		while(connections.size() > 0)
		{
			it = terraeRasa.getConnections().iterator();
			while(it.hasNext())
			{
				ServerConnectionThread thread = it.next();
				if(!thread.getOpen())
				{
					it.remove();
				}
			}	
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public synchronized static void addWorldUpdate(ServerUpdate update)
	{
		Iterator<ServerConnectionThread> it = terraeRasa.getConnections().iterator();
		while(it.hasNext())
		{
			ServerConnectionThread thread = it.next();
			thread.registerWorldUpdate(update);
		}
	}
	
	/**
	 * Stops the server, writing stuff to disk and closing the server socket, before calling System.exit() to 
	 * terminate any rogue daemon threads.
	 */
	public static synchronized void closeServer()
	{
		SettingsIO.saveSettings(terraeRasa.settings);
		terraeRasa.close();
		Log.writeWithTimestamp();
		System.exit(0);
	}
	
	public synchronized ServerSettings getSettings()
	{
		return settings;
	}
	
	public static void addServerIssuedCommand(String command)
	{
		synchronized(gameEngineLock) 
		{
			terraeRasa.gameEngine.registerServerCommand(command);
		}
	}

	public static synchronized void requestClientConnectionClosed(ServerConnectionThread connection, EntityPlayer player)
	{
		terraeRasa.gameEngine.addCloseRequest(connection, player);
	}
	
	public static synchronized void requestThreadCloseByID(int id)
	{
		Iterator<ServerConnectionThread> it = terraeRasa.getConnections().iterator();
		while(it.hasNext())
		{
			ServerConnectionThread thread = it.next();
			if(thread.getAssociatedPlayerID() == id)
			{
				thread.close();
			}
		}
	}
	
	public static synchronized void closeClientThread(ServerConnectionThread connection, EntityPlayer player)
	{
		terraeRasa.connections.remove(connection);
		synchronized(gameEngineLock) 
		{
			terraeRasa.gameEngine.removePlayer(player);
		}
		Log.log(player.getName() + " has left the game.");
		TerraeRasa.addServerIssuedCommand("/say " + EnumColor.YELLOW.toString() + " " + player.getName() + " has left the game.");
	}
	
	public static synchronized void stop()
	{
		done = true;
	}
}
