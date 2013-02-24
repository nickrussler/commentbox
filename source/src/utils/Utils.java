package utils;

import java.util.logging.Logger;

public class Utils {
	public static void log(String source, String msg) {
		Logger.getLogger(source).info(msg);
	}
}