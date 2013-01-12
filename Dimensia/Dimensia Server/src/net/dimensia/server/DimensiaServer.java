package net.dimensia.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

public class DimensiaServer 
{
	private DimensiaServer() throws IOException
	{
		serverSocket = new ServerSocket(55556);
	}
	
	public static void main(String[] args) throws IOException
	{
		server = new DimensiaServer();
		server.start();
	}
	
	private void start() throws IOException
	{
		log("Info", "Starting");
		
		while(!done)
		{
			Socket socket = serverSocket.accept(); 
		
			log("Info", "Did a thing");
		
		}
	}
	
	public static void log(String messageType, String message)
	{
		System.out.println(getTimeAsString() + " [" + messageType + "]: " + message);
	}
	
	public static void log(String message)
	{
		System.out.println(getTimeAsString() + " [Info]: " + message);
	}
	
	private static String getTimeAsString()
	{
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
	}
	
	public static boolean done;
	private static DimensiaServer server;
	private ServerSocket serverSocket;
}
