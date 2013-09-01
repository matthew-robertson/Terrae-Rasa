package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import transmission.CloseRequest;
import transmission.ServerUpdate;
import entities.EntityPlayer;
import enums.EnumColor;


public class TerraeRasa 
{
	private static final Object connectionsLock = new Object();
	private static final Object settingsLock = new Object();
	private static final Object closeRequestsLock = new Object();
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
	public volatile static boolean canAcceptConnections = false;
	private static Vector<CloseRequest> closeRequests = new Vector<CloseRequest>();
	
	public static void main(String[] args)
	{
		terraeRasa = new TerraeRasa();
		terraeRasa.run();
	}
	
	private void run()
	{
		Log.log("Terrae Rasa Server " + VERSION);
		Log.log("Edit the server.properties file for advanced configurations.");
		settings = SettingsIO.loadSettings();	

		this.gameEngine = new GameEngine(settings.universeName);
		gameEngineThread = new GameEngineThread(gameEngine);
		gameEngineThread.start();

		consoleInputThread = new ConsoleInputThread();
		consoleInputThread.start();

		//Deny connections (by blocking this thread) until the world loads.
		while(!canAcceptConnections)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
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
		else if(val.startsWith("/connect"))
		{
			String[] message = { "" };
			boolean allowed = SecurityManager.verifyConnectionIsAllowed(settings, socket, message);
			Log.log("[" + socket.getInetAddress() + "] : " + message[0]);
			
			String password = val.substring(val.indexOf(" ") + 1);
			boolean passwordMatches = true;
			if(settings.usePassword)
			{
				passwordMatches = settings.passwordCorrect(password);
				Log.log("[" + socket.getInetAddress() + "] : " + password + " matches= " + passwordMatches);
			}
			
			if(passwordMatches && allowed && connections.size() < settings.maxPlayers)
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
	
	private void registerGameConnectionThread(Socket socket, ObjectOutputStream os, ObjectInputStream is)
	{
		synchronized(connectionsLock)
		{
			ServerConnectionThread thread = new ServerConnectionThread(new WorldLock(gameEngine), socket, os, is);
			connections.add(thread);
			thread.start();	
		}
	}
			
	/**
	 * Gets the game version for the current launch
	 * @return the game version for the current launch
	 */
	public final static String getVersion()
	{
		return VERSION;
	}

	public final static String getBasePath()
	{
		return basePath;
	}
	
	public Vector<ServerConnectionThread> getConnections()
	{
		synchronized(connectionsLock)
		{
			return connections;
		}
	}
	
	private void closeSocketAndConnections()
	{
		synchronized(connectionsLock)
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
	}
	
	public static void addWorldUpdate(ServerUpdate update)
	{
		synchronized(connectionsLock)
		{
			Iterator<ServerConnectionThread> it = terraeRasa.getConnections().iterator();
			while(it.hasNext())
			{
				ServerConnectionThread thread = it.next();
				thread.registerWorldUpdate(update);
			}
		}
	}
	
	/**
	 * Stops the server, writing stuff to disk and closing the server socket, before calling System.exit() to 
	 * terminate any rogue daemon threads.
	 */
	public static void close()
	{
		done = true;
		SettingsIO.saveSettings(terraeRasa.settings);
		terraeRasa.closeSocketAndConnections();
		Log.writeWithTimestamp();
		System.exit(0);
	}
		
	public ServerSettings getSettings()
	{
		synchronized(settingsLock)
		{
			return settings;
		}
	}
	
	/**
	 * Adds a command that was issued to the server to the game engine to process.
	 * @param command the command to issue
	 */
	public static void addServerIssuedCommand(String command)
	{
		synchronized(gameEngineLock) 
		{
			terraeRasa.gameEngine.registerServerCommand(null, command, true);
		}
	}

	public static void requestClientConnectionClosed(ServerConnectionThread connection, EntityPlayer player)
	{		
		synchronized(closeRequestsLock)
		{
			closeRequests.add(new CloseRequest(connection, player));
		}
	}
	
	/**
	 * Requests a connection thread close by player ID
	 * @param id the player ID to close a thread with
	 */
	public static void requestThreadCloseByID(int id)
	{
		synchronized(connectionsLock)
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
	}
	
	private static void closeClientThread(ServerConnectionThread connection, EntityPlayer player)
	{
		synchronized(closeRequestsLock)
		{
			terraeRasa.connections.remove(connection);
			synchronized(gameEngineLock) 
			{
				terraeRasa.gameEngine.removePlayer(player);
			}
			Log.log(player.getName() + " has left the game.");
			addServerIssuedCommand("/say " + EnumColor.YELLOW.toString() + " " + player.getName() + " has left the game.");
		}
	}
	
	/**
	 * Kicks a player from the server with the given name.
	 * @param name the name of the player to kick
	 */
	public static void kick(String name)
	{
		synchronized(gameEngineLock) 
		{
			EntityPlayer player = terraeRasa.gameEngine.getPlayer(name);
			if(player != null)
			{
				requestThreadCloseByID(player.entityID);			
				String message = "Kicked " + player.getName();			
				Log.broadcast(message);
			}
		}
	}
	
	public static void processCloseRequests()
	{
		synchronized(closeRequestsLock)
		{
			CloseRequest[] requests = new CloseRequest[closeRequests.size()];
			closeRequests.copyInto(requests);
			closeRequests.clear();
			for(CloseRequest request : requests)
				TerraeRasa.closeClientThread(request.thread, request.player);
		}
	}

	public static void stop()
	{
		done = true;
	}
}
