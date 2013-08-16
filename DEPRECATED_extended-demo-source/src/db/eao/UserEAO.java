package db.eao;

import javax.ejb.Stateless;

import db.eao.local.UserEAOLocal;
import db.entity.User;

@Stateless
public class UserEAO extends BaseEAO<User> implements UserEAOLocal {
	public User findUserByUsername(String username) {
		String myQuery = "select u from User u where UPPER(u.username) like :username";
		try {
			return em.createQuery(myQuery, User.class).setParameter("username", username.toUpperCase()).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
