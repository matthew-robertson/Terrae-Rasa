package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

/**
 * Log records console output, and prints it out in a formated way. The method {@link #log(String)} has this functionality. 
 * Calling {@link #writeWithTimestamp()} writes the server log to file.
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
			String filepath = TerraeRasa.getBasePath() + "/last_server_log.txt";
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
}
