package utils;

import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HashUtils {
	private static final String salt_key = "saltkey454yektlas";

	private static String hex(byte[] data) {

		StringBuilder sb = new StringBuilder();

		for (byte b : data) {
			sb.append(Character.forDigit((b & 240) >> 4, 16));
			sb.append(Character.forDigit((b & 15), 16));
		}

		return sb.toString();
	}

	public static String md5WithSalt(String data) {
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(salt_key.getBytes("UTF-8"), "HmacSHA1"));
			mac.update(data.getBytes("UTF-8"));
			return hex(mac.doFinal());
		} catch (Exception e) {
			return data;
		}
	}

	public static String generateVerificationCode() {
		return md5WithSalt(UUID.randomUUID().toString().substring(0, 15));
	}
}
