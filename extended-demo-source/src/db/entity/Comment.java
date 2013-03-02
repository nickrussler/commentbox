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
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Article article;
	
	@ManyToOne
	private Comment answered;
	
	@Lob
	@Column(length=2000)
	private String comment;
	
	@Column
	private long creation_time;
	
	@Column
	private long modification_time;
	
	@Column
	private int likecount;
	
	@Column
	private int spamcount;
	
	@Column
	private boolean spam;
	
	@OneToMany(mappedBy = "comment")
	private List<CommentReport> commentReports;
	
	@OneToMany(mappedBy = "answered")
	private List<Comment> answers;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Comment getAnswered() {
		return answered;
	}

	public void setAnswered(Comment answered) {
		this.answered = answered;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreation_time() {
		return new Date(creation_time);
	}

	public void setCreation_time(Date creation_time) {
		this.creation_time = creation_time.getTime();
	}

	public Date getModification_time() {
		return new Date(modification_time);
	}

	public void setModification_time(Date modification_time) {
		this.modification_time = modification_time.getTime();
	}

	public int getLikecount() {
		return likecount;
	}

	public void setLikecount(int likecount) {
		this.likecount = likecount;
	}

	public int getSpamcount() {
		return spamcount;
	}

	public void setSpamcount(int spamcount) {
		this.spamcount = spamcount;
	}

	public boolean isSpam() {
		return spam;
	}

	public void setSpam(boolean spam) {
		this.spam = spam;
	}

	public List<CommentReport> getCommentReports() {
		return commentReports;
	}

	public void setCommentReports(List<CommentReport> commentReports) {
		this.commentReports = commentReports;
	}

	public List<Comment> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Comment> answers) {
		this.answers = answers;
	}
}
