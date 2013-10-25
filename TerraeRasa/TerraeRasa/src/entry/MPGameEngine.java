package entry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import server.ConsoleInputThread;
import server.Log;
import server.ServerConnectionThread;
import server.ServerSettings;
import server.SettingsIO;
import server.WorldLock;
import server.entities.EntityPlayer;
import transmission.CloseRequest;
import transmission.ServerUpdate;
import enums.EnumColor;


public class MPGameEngine extends Thread
{
	private static final Object connectionsLock = new Object();
	private static final Object settingsLock = new Object();
	private static final Object closeRequestsLock = new Object();
	//http://stackoverflow.com/questions/2914375/getting-file-path-in-java
	public volatile static boolean done = false;
	private ServerSettings settings;
	private Vector<ServerConnectionThread> connections = new Vector<ServerConnectionThread>();
	public MPGameLoop gameEngine;
	public volatile static MPGameEngine terraeRasa;
	private ServerSocket serverSocket;
	private static ConsoleInputThread consoleInputThread = new ConsoleInputThread();;
	private static Object gameEngineLock = new Object();
	public volatile static boolean canAcceptConnections = false;
	private static Vector<CloseRequest> closeRequests = new Vector<CloseRequest>();
	/**Note:This parameter is null unless initiated in SP Mode.*/
	private String universeName = null;
	
	public MPGameEngine(String selectedUniverseName) {
		this.universeName = selectedUniverseName;
		setName("MPGameEngine");
	}
	
	public MPGameEngine() {
		setName("MPGameEngine");
	}

	public String getUniverseName()
	{
		return universeName;
	}

	public void run()
	{
		done = false;
		Log.log("Terrae Rasa Server " + TerraeRasa.getVersion());
		Log.log("Edit the server.properties file for advanced configurations.");
		settings = SettingsIO.loadSettings();	
		if(settings == null) 
		{
			settings = new ServerSettings();
		}
		//TODO: [mild-lazy] The line of code below here indicates a bit of laziness. Settings are wierd in SP.
		this.gameEngine = new MPGameLoop((universeName != null) ? this.universeName : settings.universeName);
		gameEngine.start();

		if(!consoleInputThread.isAlive())
		{
			consoleInputThread.start();
		}

		//Deny connections (by blocking this thread) until the world loads.
		while(!canAcceptConnections)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		TerraeRasa.isMPServerRunning = true;
		
		try {
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(settings.port)); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(!done)
		{
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				Log.log("Closing the server connection.");
//				e.printStackTrace();
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
			os.writeObject(new String[] { TerraeRasa.getVersion(), connections.size() + "/" + settings.maxPlayers + " Players", settings.serverMessage, "You connected with: " + socket.getInetAddress() });
			os.flush();
		}
		else if(val.startsWith("/connect"))
		{
			String[] message = { "" };
			boolean allowed = server.SecurityManager.verifyConnectionIsAllowed(settings, socket, message);
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
	public static void kill()
	{
		done = true;
		SettingsIO.saveSettings(terraeRasa.settings);
		terraeRasa.closeSocketAndConnections();
		Log.writeWithTimestamp();
		terraeRasa.gameEngine.getChunkManager().killThreadpool();
		//---program should be terminated by this point. If it is not then a 
		//---thread has not been properly terminated. 
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
				MPGameEngine.closeClientThread(request.thread, request.player);
		}
	}

	/**
	 * THIS DOES NOT FULLY STOP THE SERVER!
	 */
	public static void finish()
	{
		done = true;
	}
}
