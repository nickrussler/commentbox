package db.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	private User creator;
	
	@Column
	private String title;
	
	@Lob
	@Column(length=9000)
	private String article;

	@Column
	private long creation_time;

	@Column
	private String picture_filename;

	@Column
	private String editingpassword;
	
	@Column
	private String websitelink;

	@Column
	private int likecount;
	
	@Column
	private int dislikecount;

	@Column
	private int totallikecount;
	
	@Column
	private int commentcount;
	
	@Column
	private int rootcommentcount;
	
	@OneToMany(mappedBy = "article")
	private List<Comment> comments;
	
	@OneToMany(mappedBy = "article")
	private List<ArticleReport> articleReports;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getCreation_time() {
		return new Date(creation_time);
	}

	public void setCreation_time(Date creation_time) {
		this.creation_time = creation_time.getTime();
	}

	public String getPicture_filename() {
		return picture_filename;
	}

	public void setPicture_filename(String picture_filename) {
		this.picture_filename = picture_filename;
	}

	public int getLikecount() {
		return likecount;
	}

	public void setLikecount(int likecount) {
		this.likecount = likecount;
	}

	public int getDislikecount() {
		return dislikecount;
	}

	public void setDislikecount(int dislikecount) {
		this.dislikecount = dislikecount;
	}

	public int getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(int commentcount) {
		this.commentcount = commentcount;
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

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEditingpassword() {
		return editingpassword;
	}

	public void setEditingpassword(String editingpassword) {
		this.editingpassword = editingpassword;
	}

	public int getTotallikecount() {
		return totallikecount;
	}

	public void setTotallikecount(int totallikecount) {
		this.totallikecount = totallikecount;
	}

	public String getWebsitelink() {
		return websitelink;
	}

	public void setWebsitelink(String websitelink) {
		this.websitelink = websitelink;
	}

	public int getRootcommentcount() {
		return rootcommentcount;
	}

	public void setRootcommentcount(int rootcommentcount) {
		this.rootcommentcount = rootcommentcount;
	}
}
