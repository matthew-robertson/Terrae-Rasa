package net.dimensia.src;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper
{
	public String getDateAsYYYYMMDD()
	{
		Date dateNow = new Date ();
		SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
		StringBuilder nowYYYYMMDD = new StringBuilder( dateformatYYYYMMDD.format( dateNow ) );
		return nowYYYYMMDD.toString();
	}	

	public String getDateAndTime()
	{
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
	    String strDate = sdf.format(cal.getTime());
	    return strDate;
	}
	
}
