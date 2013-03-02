package db.enums;

import java.util.HashMap;
import java.util.Map;

public enum Reporttype {
	Like("Like", 'L'), Dislike("Dislike", 'D'), Spam("Spam", 'S'), Expired("Expired", 'E');
	
	private String displaytext;
	private Character dbKey;
	
	private static final Map<Character, Reporttype> lookupDbKey = new HashMap<Character, Reporttype>();
	
	static {
		for (Reporttype k : values()) {
			lookupDbKey.put(k.getDbKey(), k);
		}
	}

	private Reporttype(String displaytext, Character dbKey) {
		this.displaytext = displaytext;
		this.dbKey = dbKey;
	}

	public Character getDbKey() {
		return dbKey;
	}
	
	public String getDisplaytext() {
		return displaytext;
	}

	public static Reporttype findByDbKey(Character dbKey) {
		return lookupDbKey.get(dbKey);
	}
}
