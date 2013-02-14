package net.dimensia.src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.dimensia.client.Dimensia;

public class ErrorUtils 
{
	private final String BASE_PATH;
	
	public ErrorUtils()
	{
		String osName = System.getProperty("os.name").toLowerCase();
		if(osName.contains("window"))
		{
			BASE_PATH = Dimensia.WINDOWS_BASE_PATH;
		}
		else if(osName.contains("mac"))
		{
			BASE_PATH = Dimensia.MAC_BASE_PATH;
		}
		else if(osName.contains("ubuntu") || osName.contains("linux"))
		{
			BASE_PATH = Dimensia.LINUX_BASE_PATH;
		}
		else
		{
			throw new RuntimeException("Invalid OS");
		}
	}
	
	public void writeErrorToFile(Exception e, boolean fatal) 
	{
		try
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String stackTrace = sw.toString(); // stack trace as a string
			
			DateHelper helper = new DateHelper();
			
			String time = helper.getDateAndTime();
			time = time.replace(':', '.');
			time = time.replace('/', '.');
			
			File file = new File(BASE_PATH + "/Errors");
			file.mkdir();
			String filename = BASE_PATH + "/Errors/Stack_Trace" + ((fatal) ? "_FATAL_" : "_NONFATAL_") + time;
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));
			writer.write(stackTrace);
			writer.flush();
			writer.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}	
	}
	
	
}