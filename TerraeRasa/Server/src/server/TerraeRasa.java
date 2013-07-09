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
		System.out.println(val + " From " + socket.getInetAddress());
		
		if(val.equals("/serverinfo"))
		{
			os.writeObject(new String[] { VERSION, connections.size() + "/" + settings.maxPlayers + " Players", settings.serverMessage, "You connected with: " + socket.getInetAddress() });
		}
		else if(val.equals("/connect"))
		{
			boolean allowed = SecurityManager.verifyConnectionIsAllowed(settings, socket);
			if(allowed)
			{
				os.writeUTF("connection accepted");
				os.flush();
				registerGameConnectionThread(socket);
			}
			else
			{
				os.writeUTF("connection not allowed");
				os.flush();
			}
		}
		
		os.close();
		is.close();
	}
	
	private void registerGameConnectionThread(Socket socket)
	{
		ServerConnectionThread thread = new ServerConnectionThread(new WorldLock(), socket);
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
	}
	
	public synchronized ServerSettings getSettings()
	{
		return settings;
	}

}