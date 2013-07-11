package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class TerraeRasa 
{
	//http://stackoverflow.com/questions/2914375/getting-file-path-in-java
	private static String basePath = "/home/alec/terraerasaserver";
	public static boolean done = false;
	private final static String VERSION = "Alpha 0.1.4";	
	private ServerSettings settings;
	private Vector<ServerConnectionThread> connections = new Vector<ServerConnectionThread>();
	public GameEngine gameEngine;
	public static TerraeRasa terraeRasa;
	private GameEngineThread gameEngineThread;
	private ServerSocket serverSocket;
	
	public static void main(String[] args)
	{
		terraeRasa = new TerraeRasa();
		terraeRasa.run();
	}
	
	public void run()
	{
		settings = SettingsIO.loadSettings();	

		this.gameEngine = new GameEngine(settings.universeName);
		gameEngineThread = new GameEngineThread(gameEngine);
		gameEngineThread.start();

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
			
			try {
				handleSocketConnection(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}

		//Cleanup
		//Issue this from the game engine thread.
		SettingsIO.saveSettings(settings);
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
	
	private void registerGameConnectionThread(Socket socket, ObjectOutputStream os, ObjectInputStream is)
	{
		ServerConnectionThread thread = new ServerConnectionThread(new WorldLock(gameEngine), socket, os, is);
		connections.add(thread);
		thread.start();	
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
	
	
	public static synchronized void close()
	{
		SettingsIO.saveSettings(terraeRasa.settings);
		//Close connections to clients
		//close the server socket
		System.exit(0);
	}
	
	public synchronized ServerSettings getSettings()
	{
		return settings;
	}

}
