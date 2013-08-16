package db.enums;

import java.util.HashMap;
import java.util.Map;

public enum Usertype {
	Admin("Admin", 'A'), User("User", 'U');
	
	private String displaytext;
	private Character dbKey;
	
	private static final Map<Character, Usertype> lookupDbKey = new HashMap<Character, Usertype>();
	
	static {
		for (Usertype k : values()) {
			lookupDbKey.put(k.getDbKey(), k);
		}
	}

	private Usertype(String displaytext, Character dbKey) {
		this.displaytext = displaytext;
		this.dbKey = dbKey;
	}

	public Character getDbKey() {
		return dbKey;
	}
	
	public String getDisplaytext() {
		return displaytext;
	}

	public static Usertype findByDbKey(Character dbKey) {
		return lookupDbKey.get(dbKey);
	}
}
