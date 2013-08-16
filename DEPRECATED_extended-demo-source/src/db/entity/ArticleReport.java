package db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import db.enums.Reporttype;

@Entity
public class ArticleReport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Article article;
	
	@Column
	private char reporttype;

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

	public Reporttype getReporttype() {
		return Reporttype.findByDbKey(this.reporttype);
	}

	public void setReporttype(Reporttype reporttype) {
		this.reporttype = reporttype.getDbKey();
	}
}
