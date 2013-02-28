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
	private int commentsPerPage = 5;
	private Long commentsPage = 1L;
	private int commentCount;
	private int rootcommentCount;
	
	private List<Comment> comments;
	// Comments End
	
	@PostConstruct
	public void init() {	
		comments = dbSimulator.getComments();
	}
	
	public void onFetchNewComments() {	
		comments = dbSimulator.getComments();
	}
	
	public void onFetchNewAnswers(Comment comment) {
		// Nothing to do here because the list is Application scoped
	}
	
	public boolean onCreateComment(Comment comment) {
		// Nothing to do here, except assingning a unique id, because the list is Application scoped
		comment.setId(dbSimulator.getUniqueID() + "");
		dbSimulator.setUniqueID(dbSimulator.getUniqueID() + 1);
		
		return true;
	}
	
	public void onCommentLike(Comment commentBean2) {
		// Nothing to do here because the list is Application scoped
	}
	
	public void onCommentSpam(Comment commentBean) {
		// Nothing to do here because the list is Application scoped
	}
	
	public boolean onCommentEdit(Comment commentBean) {		
		// Nothing to do here because the list is Application scoped
		
		return true;
	}
	
	public void onCommentDelete(Comment commentBean) {
		// Nothing to do here because the list is Application scoped
	}
	
	public void onPageChange(Long newPage) {
		this.commentsPage = newPage;	

		int start = (commentsPage.intValue() - 1) * commentsPerPage;
		int end = start + commentsPerPage;
		
		comments = dbSimulator.getComments().subList(start, end);
	}
	
	public void vote(long article_id, boolean like, boolean isView) {
		// Nothing to do here because the list is Application scoped
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

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	
	public int getCommentsPerPage() {
		return commentsPerPage;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public int getRootcommentCount() {
		return rootcommentCount;
	}

	public void setRootcommentCount(int rootcommentCount) {
		this.rootcommentCount = rootcommentCount;
	}

	public Long getCommentsPage() {
		return commentsPage;
	}

	public void setCommentsPage(Long commentsPage) {
		this.commentsPage = commentsPage;
	}
}
