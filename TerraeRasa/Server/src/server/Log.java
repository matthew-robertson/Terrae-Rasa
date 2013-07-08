package server;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log 
{
	public static void log(String message)
	{
		System.out.println(getServerTimeStamp() + " " + message);
	}
	
	private static String getServerTimeStamp()
	{
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    String strDate = sdf.format(cal.getTime());
	    return strDate;
	}
}
