package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static long getNow() {
		return (new Date()).getTime();
	}
	
	public static Date getNowAsDate() {
		return new Date();
	}
	
	//dd.MM.yyyy
	public static Date StringToDate(String string) throws Exception{
		SimpleDateFormat sdfDatum = new SimpleDateFormat("dd.MM.yyyy");
		return sdfDatum.parse(string);
	}
	
	//dd.MM.yyyy
	public static String DateToString(Date date){
		SimpleDateFormat sdfDatum = new SimpleDateFormat("dd.MM.yyyy");
		return sdfDatum.format(date);		
	}	
}
