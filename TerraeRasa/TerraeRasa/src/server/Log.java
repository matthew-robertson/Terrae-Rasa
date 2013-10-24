package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import entry.MPGameEngine;
import entry.TerraeRasa;
import enums.EnumColor;

/**
 * Log records console output, and prints it out in a formated way. The method {@link #log(String)} has this functionality. 
 * If a command should both be written to the server console and broadcast to all clients, then the method {@link #broadcast(String)}
 * should be used. Calling {@link #writeWithTimestamp()} writes the server log to file.
 * @author      Alec Sobeck
 * @author      Matthew Robertson
 * @version     1.0
 * @since       1.0
 */
public class Log 
{
	private static Vector<String> serverLog = new Vector<String>(40);
	
	/**
	 * Log is static, it cannot be initialized
	 */
	private Log()
	{	
	}

	/**
	 * Writes a message to console with a date and time stamp, and then records it in the server log which is writen to file when
	 * the server is stopped.
	 * @param message the message to log
	 */
	public static void log(String message)
	{
		String output = getServerTimeStamp() + " " + message;
		System.out.println(output);
		serverLog.add(output);
	}
	
	/**
	 * Gets a current timestamp for when a command was issued
	 * @return the current time in the form DD-MM-YYYY HH-MM-SS
	 */
	private static String getServerTimeStamp()
	{
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    String strDate = sdf.format(cal.getTime());
	    return strDate;
	}

	/**
	 * Writes the server log to disk, under the filename 'last_server_log.txt'. This is located in the main directory
	 * of the server alongside the jar.
	 */
	public static void writeWithTimestamp()
	{
		try {
			String folderPath = TerraeRasa.getBasePath() + "/logs";
			new File(folderPath).mkdir();
			
			String filepath = folderPath + "/" + "[log]: session-ending-at: "+ getServerTimeStamp() + ".txt";
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filepath)));
			Iterator<String> it = serverLog.iterator();
			while(it.hasNext())
			{
				String next = it.next();
				writer.write(next + '\n');
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a message to console with a date and time stamp, and then records it in the server log. After this, the message
	 * is broadcast to the client.
	 * @param message the message to issue
	 */
	public static void broadcast(String message)
	{
		log(message);
		MPGameEngine.addServerIssuedCommand("/say " + EnumColor.YELLOW.toString() + " " + message);	
	}
}
