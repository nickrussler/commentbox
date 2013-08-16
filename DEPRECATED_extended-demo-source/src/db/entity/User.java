package db.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import db.enums.Usertype;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String username;
	
	@Column
	private String password_hash;
	
	@Column
	private String email;
	
	@Column
	private String avatar_filename;
	
	@Column
	private char usertype;
	
	@Column
	private long creation_time;
	
	@OneToMany(mappedBy = "creator")
	private List<Article> articles;
	
	@OneToMany(mappedBy = "user")
	private List<Comment> comments;
	
	@OneToMany(mappedBy = "user")
	private List<ArticleReport> articleReports;
	
	@OneToMany(mappedBy = "user")
	private List<CommentReport> commentReports;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar_filename() {
		return avatar_filename;
	}

	public void setAvatar_filename(String avatar_filename) {
		this.avatar_filename = avatar_filename;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<ArticleReport> getArticleReports() {
		return articleReports;
	}

	public void setArticleReports(List<ArticleReport> articleReports) {
		this.articleReports = articleReports;
	}

	public List<CommentReport> getCommentReports() {
		return commentReports;
	}

	public void setCommentReports(List<CommentReport> commentReports) {
		this.commentReports = commentReports;
	}

	public Usertype getUsertype() {
		return Usertype.findByDbKey(this.usertype);
	}

	public void setUsertype(Usertype usertype) {
		this.usertype = usertype.getDbKey();
	}

	public Date getCreation_time() {
		return new Date(creation_time);
	}

	public void setCreation_time(Date creation_time) {
		this.creation_time = creation_time.getTime();
	}
}