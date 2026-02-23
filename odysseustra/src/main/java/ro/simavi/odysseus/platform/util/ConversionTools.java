package ro.simavi.odysseus.platform.util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.Map;
import java.util.Map.Entry;


public class ConversionTools {
	
	public static final String DateFormat = "dd/MM/yyyy";
	public static final String SmallDateFormat = "dd/MM/yy";
	public static final String DateTimeFormat = "dd/MM/yyyy HH:mm:ss";
	public static final String DateTimeFormatSmall = "dd/MM/yyyy HH:mm";
	public static final String DateTimeFormatWebservice = "yyyy-MM-dd_HH-mm-ss";
	public static final String DateTimeFormatSQL = "yyyy-MM-dd HH:mm:ss";
	public static final String DateTimeFormatJS = "yyyy, MM, dd, HH, mm, ss";
	public static final String IPMDateFormat = "yyyy-MM-dd";
	public static final String TimeFormat = "HH:mm:ss";
		
	public static int getValue(Object num)
	{
		if (num == null) return 0;
		
		if (num instanceof Integer) return (Integer) num;
		if (num instanceof Byte) return ((Byte) num).intValue();
		if (num instanceof Long) return ((Long) num).intValue();
		if (num instanceof BigInteger) return ((BigInteger) num).intValue();	
		if (num instanceof BigDecimal) return ((BigDecimal) num).intValue();	
		if (num instanceof String) return Integer.parseInt((String) num);
		
		try {
			num = ((Object[])num)[0];
			if (num instanceof Integer) return (Integer) num;
			if (num instanceof Byte) return ((Byte) num).intValue();
			if (num instanceof BigInteger) return ((BigInteger) num).intValue();	
			if (num instanceof BigDecimal) return ((BigDecimal) num).intValue();
			if (num instanceof String) return Integer.parseInt((String) num);	
		} catch (Exception e)
		{
			//ignore
		}
		
		return 0;
	}
	
	public static Date getDate(String input)
	{
		if (input == null) return null;
		Date result = null;
		try
		{
			if (input.length() == 19)
			{
				result = Tools.parseDateString(input, DateTimeFormatSQL);
			} else if (input.length() == 8)
			{
				result = Tools.parseDateString(input, SmallDateFormat);
			} else if (input.length() == 10) {
				if (input.contains("-"))
				{
					result = Tools.parseDateString(input, IPMDateFormat);
				} else {
					result = Tools.parseDateString(input, DateFormat);
				}
			}
		} catch (Exception e)
		{
			return null;
		}
		
		if (result != null)
		{
			//remove invalid dates that are not allowed by MySQL
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 9999);
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			cal.set(Calendar.DAY_OF_MONTH, 1);			
			Date max = cal.getTime();			
					
			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 1000);
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);	
			Date min =cal.getTime();
			
			if (result.after(max) || result.before(min)) {
				return null;
			}
		}
		
		return result;
	}
		
	public static String getFullString(Date input) {
		if (input == null) return null;
		try
		{
			return Tools.formatDate(input, DateTimeFormat);
		} catch (Exception e)
		{
			return null;
		}
	}
	
	public static String getFullString4Webservice(Date input) {
		if (input == null) return null;
		try
		{
			return Tools.formatDate(input, DateTimeFormatWebservice);
		} catch (Exception e)
		{
			return null;
		}
	}
	
	public static String getFullStringSmall(Date input) {
		if (input == null) return null;
		try
		{
			return Tools.formatDate(input, DateTimeFormatSmall);
		} catch (Exception e)
		{
			return null;
		}
	}
	
	public static Integer getInt(String input) {
	    return getInt(input, null);
	}
	
	public static Integer getInt(String input, Integer defaultValue) {
	    try {
		    return new Integer(input);
	    } catch (NumberFormatException e) {
		    return defaultValue;
	    }
	}

	public static String escape(String input)
	{
		return StringEscapeUtils.escapeXml11(input);
	}

	public static String mapToHTML(Map<Integer,String> map) {
		StringBuilder result = new StringBuilder();
		result.append("<!DOCTYPE html>");
		result.append("<html>");
		result.append("<head>");
		result.append("</head>");
		result.append("<body>");
		
		for ( Entry<Integer, String> entry : map.entrySet()) {
			result.append("<article id=\"");
			result.append(entry.getKey().toString());
			result.append("\">");
			result.append(entry.getValue());
			result.append("</article>");
		}
		result.append("</body>");
		result.append("</html>");
        return result.toString();
	}

	public static String getStringForBytes(long byteslong) {
		double bytes = byteslong;
		DecimalFormat df = new DecimalFormat("###.##");
		
		if (bytes < 1024) return bytes + " B";		
		bytes = bytes / 1024;
		if (bytes < 1024) return df.format(bytes) + " KB";
		bytes = bytes / 1024;
		if (bytes < 1024) return df.format(bytes) + " MB";
		bytes = bytes / 1024;
		return df.format(bytes) + " GB";
	}

}
