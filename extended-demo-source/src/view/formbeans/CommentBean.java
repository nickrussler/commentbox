package view.formbeans;

import java.util.Date;

public class CommentBean {
	private String user_avatar_filename;
	private String user_username;
	private boolean user_isAdmin=false;
	private String comment_text;
	private long comment_id;
	private int like_count;
	private Date modification_time;

	public String getUser_avatar_filename() {
		return user_avatar_filename;
	}

	public void setUser_avatar_filename(String user_avatar_filename) {
		this.user_avatar_filename = user_avatar_filename;
	}

	public String getUser_username() {
		return user_username;
	}

	public void setUser_username(String user_username) {
		this.user_username = user_username;
	}

	public String getComment_text() {
		return comment_text;
	}

	public void setComment_text(String comment_text) {
		this.comment_text = comment_text;
	}

	public long getComment_id() {
		return comment_id;
	}

	public void setComment_id(long comment_id) {
		this.comment_id = comment_id;
	}

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	public Date getModification_time() {
		return modification_time;
	}

	public void setModification_time(Date modification_time) {
		this.modification_time = modification_time;
	}

	public boolean isUser_isAdmin() {
		return user_isAdmin;
	}

	public void setUser_isAdmin(boolean user_isAdmin) {
		this.user_isAdmin = user_isAdmin;
	}
}
