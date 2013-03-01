package utils;
/* 
 * License:
 * This sourcecode (SerializationUtils.java) is license free. 
 * This means you are free to do whatever you want with it, without asking the Authors.
 */
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
 
/**
 * Simple Class to serialize object to byte arrays
 * @author
 * Nick Russler
 * http://www.whitebyte.info
 */
public class SerializationUtils {
 
 
	/**
	 * @param obj - object to serialize to a byte array
	 * @return byte array containing the serialized obj
	 */
	public static byte[] serialize(Object obj) {
		byte[] result = null;
		ByteArrayOutputStream fos = null;
 
		try {
			fos = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(fos);
			o.writeObject(obj);
			result = fos.toByteArray();
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
 
		return result;
	}
 
 
	/**
	 * @param arr - the byte array that holds the serialized object
	 * @return the deserialized object
	 */
	public static Object deserialize(byte[] arr) {
		InputStream fis = null;
 
		try {
			fis = new ByteArrayInputStream(arr);
			ObjectInputStream o = new ObjectInputStream(fis);
			return o.readObject();
		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
 
		return null;
	}
 
	/**
	 * @param obj - object to be cloned
	 * @return a clone of obj
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cloneObject(T obj) {		
		return (T) deserialize(serialize(obj));
	}
}