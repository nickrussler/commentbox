package view.formbeans;

import java.util.Date;

public class ArticleBean {
	private Long article_id;

	private String title;
	private String articletext;	
	private String websitelink;
	private String photolink;
	private String photofilepath;
	private String username;
	
	private Date creation_time;

	private String title2;

	private int likecount;
	private int dislikecount;
	private int commentcount;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArticletext() {
		return articletext;
	}

	public void setArticletext(String articletext) {
		this.articletext = articletext;
	}

	public String getPhotolink() {
		return photolink;
	}

	public void setPhotolink(String photolink) {
		this.photolink = photolink;
	}

	public String getWebsitelink() {
		return websitelink;
	}

	public void setWebsitelink(String websitelink) {
		this.websitelink = websitelink;
	}

	public String getPhotofilepath() {
		return photofilepath;
	}

	public void setPhotofilepath(String photofilepath) {
		this.photofilepath = photofilepath;
	}

	public int getLikecount() {
		return likecount;
	}

	public int getLikecountPositive() {
		if (likecount < 0) {
			return likecount * (-1);
		}
		return likecount;
	}

	public String getLikecountPre() {
		if (likecount <= 0) {
			return "" + likecount;
		} else {
			return "+" + likecount;
		}
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

	public String getUsername() {
		return username;
	}

	public Long getArticle_id() {
		return article_id;
	}

	public void setArticle_id(Long article_id) {
		this.article_id = article_id;
	}

	public int getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(int commentcount) {
		this.commentcount = commentcount;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public Date getCreation_time() {
		return creation_time;
	}

	public void setCreation_time(Date creation_time) {
		this.creation_time = creation_time;
	}
}
