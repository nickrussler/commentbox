package db.eao.local;

import db.entity.User;

public interface UserEAOLocal extends BaseEAOLocal<User> {
	public User findUserByUsername(String username);
}
