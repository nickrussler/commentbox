package view.backingbeans;

import info.whitebyte.component.commentbox.Comment;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import view.formbeans.ArticleBean;

@ManagedBean
@ViewScoped
public class ArticleView implements Serializable{
	private static final long serialVersionUID = 403539584179037857L;
	
	private long article_id = 1;
	private ArticleBean articleBean;
	
	@Inject
	DatabaseSimulator dbSimulator;
	
	// Comments Begin
	private Long commentsPerPage = 5L;
	private Long commentsPage = 1L;
	private Long commentCount;
	
	private List<Comment> comments;
	// Comments End
	
	private void loadRootComments() {
		comments = dbSimulator.getRootComments((commentsPage - 1L)*commentsPerPage, commentsPerPage);
		commentCount = dbSimulator.getRootCommentCount();
	}
	
	@PostConstruct
	public void init() {	
		loadRootComments();
	}
	
	public void onFetchNewComments() {	
		loadRootComments();
	}
	
	public void onFetchNewAnswers(Comment comment) {
		comment.setAnswers(dbSimulator.getAnswers(comment));
	}
	
	public boolean onCreateComment(Comment comment) {
		dbSimulator.createComment(comment);
		
		return true;
	}
	
	public boolean onCreateAnswer(Comment comment) {
		dbSimulator.createAnswer(comment.getParent(), comment);
		
		return true;
	}
	
	public void onCommentLike(Comment comment) {
		dbSimulator.likeComment(comment);
	}
	
	public void onCommentSpam(Comment comment) {
		dbSimulator.spamComment(comment);
	}
	
	public boolean onCommentEdit(Comment comment) {		
		dbSimulator.editComment(comment);
		
		return true;
	}
	
	public void onCommentDelete(Comment comment) {
		dbSimulator.deleteComment(comment);
	}
	
	public void onPageChange(Long newPage) {
		loadRootComments();
	}
	
	public long getArticle_id() {
		return article_id;
	}

	public void setArticle_id(long article_id) {
		this.article_id = article_id;
	}

	public ArticleBean getArticleBean() {
		return articleBean;
	}

	public void setArticleBean(ArticleBean articleBean) {
		this.articleBean = articleBean;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}
	
	public Long getCommentsPerPage() {
		return commentsPerPage;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Long getCommentsPage() {
		return commentsPage;
	}

	public void setCommentsPage(Long commentsPage) {
		this.commentsPage = commentsPage;
	}
}
