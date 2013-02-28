package view.backingbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;

import utils.Constants;
import utils.DateUtils;
import utils.JSFMessage;
import utils.MessageHandler;
import utils.StripHtml;
import utils.Utils;
import view.formbeans.ArticleBean;

import com.github.slugify.Slugify;

import db.eao.local.ArticleEAOLocal;
import db.eao.local.CommentEAOLocal;
import db.eao.local.CommentReportEAOLocal;
import db.eao.local.UserEAOLocal;
import db.entity.Article;
import db.entity.Comment;
import db.entity.CommentReport;
import db.entity.User;
import db.enums.Reporttype;

@ManagedBean
@ViewScoped
public class ArticleView implements Serializable{
	private static final long serialVersionUID = 403539584179037857L;

	// EAO Begin	
	@Inject
	private UserSession userSession;

	@Inject
	private ArticleEAOLocal articleEAO;
	
	@Inject
	private CommentEAOLocal commentEAO;
	
	@Inject
	private UserEAOLocal userEAO;
	
	@Inject
	private CommentReportEAOLocal commentReportEAO;	
	// EAO End
	
	private long article_id = 1;
	private ArticleBean articleBean;	
	
	// Comments Begin	
	private int commentsPerPage = Constants.COMMENTS_PER_PAGE_COUNT;
	private Long commentsPage = 1L;
	private int commentCount;
	private int rootcommentCount;
	
	private List<info.whitebyte.component.commentbox.Comment> comments = new ArrayList<info.whitebyte.component.commentbox.Comment>();	
	// Comments End
	
	private MessageHandler getMessageHandler() {
		return userSession.getMessages()[Constants.MessageClassArticleView];
	}
	
	public void load(ComponentSystemEvent event) throws IOException {
		this.getMessageHandler().dischargeMessages();
		userSession.disChargeNavigationMessages();
		
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		
		Article article = articleEAO.findById(article_id);
		
		if (article == null) {
			userSession.getMessages()[Constants.MessageClassMain].addJSFMessage(new JSFMessage("Error", "Could not a Project with the id: " + article_id + "!"));
			Utils.RedirectTo(Constants.HOMEPAGE);
			return;
		}
		
		this.commentCount = article.getCommentcount();
		this.rootcommentCount = article.getRootcommentcount();
				
		fillFormbean(article);	
		fillComments();
	}
	
	private void fillFormbean(Article art) {
		articleBean = new ArticleBean();
		
		articleBean.setArticle_id(art.getId());		
		articleBean.setArticletext(art.getArticle());
		articleBean.setWebsitelink(art.getWebsitelink());
		articleBean.setDislikecount(art.getDislikecount());	
		articleBean.setLikecount(art.getTotallikecount() );
		articleBean.setPhotofilepath(art.getPicture_filename());
		articleBean.setPhotolink("");
		articleBean.setTitle(art.getTitle());
		articleBean.setCreation_time(art.getCreation_time());		
		
		String slugifiedURLTitle = Slugify.slugify(art.getTitle());
		
		if(slugifiedURLTitle.length()>79){
			slugifiedURLTitle=slugifiedURLTitle.substring(0,80);
		}
		
		if(slugifiedURLTitle.isEmpty()){
			slugifiedURLTitle="-";
		}else{
			if(slugifiedURLTitle.endsWith("-")){
				slugifiedURLTitle=slugifiedURLTitle.substring(0,slugifiedURLTitle.length()-2);
			}
		}
		
		articleBean.setTitle2(slugifiedURLTitle);
	}
	
	private List<info.whitebyte.component.commentbox.Comment> getChildren(Comment comment) {
		List<Comment> findByParentWithMaxSpamCount = commentEAO.findByParentWithMaxSpamCount(comment);
		
		List<info.whitebyte.component.commentbox.Comment> result = new ArrayList<info.whitebyte.component.commentbox.Comment>();
		
		for (Comment comment2 : findByParentWithMaxSpamCount) {
			User author = comment2.getUser();
			
			info.whitebyte.component.commentbox.Comment commentBean = new info.whitebyte.component.commentbox.Comment();
			commentBean.setId(comment2.getId() + "");
			commentBean.setComment_text(comment2.getComment());
			commentBean.setUser_id(author.getId() + "");
			commentBean.setUser_profile_avatar_url("/image/" + author.getAvatar_filename());
			commentBean.setUser_username(author.getUsername());
			commentBean.setLikecount(comment2.getLikecount());
			commentBean.setModification_time(comment2.getModification_time());			
			
			commentBean.setAnswers(getChildren(comment2));

			result.add(commentBean);
		}
		
		return result;
	}

	private void fillComments(int maxResults, int firstResult) {		
		Article article = this.articleEAO.findById(article_id);
		
		this.comments.clear();
		
		List<Comment> dbComments = commentEAO.findRootCommentsByArticleWithMaxSpamCount(article, maxResults, firstResult);
		
		for (Comment comment : dbComments) {			
			User author = comment.getUser();
			
			info.whitebyte.component.commentbox.Comment commentBean = new info.whitebyte.component.commentbox.Comment();
			
			commentBean.setId(comment.getId() + "");
			commentBean.setComment_text(comment.getComment());
			commentBean.setUser_id(author.getId() + "");
			commentBean.setUser_profile_avatar_url("/image/" + author.getAvatar_filename());
			commentBean.setUser_username(author.getUsername());
			commentBean.setLikecount(comment.getLikecount());
			commentBean.setModification_time(comment.getModification_time());			
			commentBean.setAnswers(getChildren(comment));
			
			this.comments.add(commentBean);
		}
	}
	
	public void fillComments() {
		fillComments(commentsPerPage, (commentsPage.intValue() - 1) * commentsPerPage);
	}
	
	public void onFetchNewComments() {
		fillComments();
	}
	
	public void onFetchNewAnswers(info.whitebyte.component.commentbox.Comment comment) {
		comment.setAnswers(this.getChildren(this.commentEAO.findById(Long.parseLong(comment.getId()))));
	}
	
	public boolean onCreateComment(info.whitebyte.component.commentbox.Comment comment) {
		boolean isAnswer = (comment.getParent() != null);
		
		Comment newComment = new Comment();
		
		Article article = this.articleEAO.findById(article_id);
		
		User user = userEAO.findById(Long.parseLong(comment.getUser_id()));
		Date now = new Date(DateUtils.getNow());
		String stripped_html = StripHtml.StripTagsAndAttributes(comment.getComment_text(), Constants.ALLOWED_TAGS_COMMENTS);
		
		if (stripped_html.length() == 0) {
			this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "You can't submit empty comments.", FacesMessage.SEVERITY_WARN));
			return false;
		}
		
		newComment.setArticle(article);
		newComment.setComment(stripped_html);
		newComment.setCreation_time(now);
		newComment.setModification_time(now);
		newComment.setUser(user);
		
		if (comment.getParent() != null) {
			newComment.setAnswered(commentEAO.findById(Long.parseLong(comment.getParent().getId())));
		}		
		
		try {			
			this.commentEAO.create(newComment);
			
			comment.setId(newComment.getId() + "");
			
			article.setCommentcount(article.getCommentcount() + 1);
			setCommentCount(getCommentCount() + 1);			
			if (!isAnswer) {
				article.setRootcommentcount(article.getRootcommentcount() + 1);
			}			
			this.articleEAO.update(article);
			
		} catch (Exception e) {
			this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", e.getMessage(), FacesMessage.SEVERITY_FATAL));
			Utils.persistantlog("createComment", e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public void onCommentLike(info.whitebyte.component.commentbox.Comment commentBean2) {
		Comment comment = commentEAO.findById(Long.parseLong(commentBean2.getId()));
		User user = userSession.getCurrentUser();
		
		if (user == null) {
			Utils.log("likeComment", "User is not logged in !");
			this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "You have to be logged in to leave a comment!", FacesMessage.SEVERITY_WARN));
			this.getMessageHandler().dischargeMessages();
			return;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("user", user);
	    paramMap.put("comment", comment);
	    paramMap.put("reporttype", Reporttype.Like.getDbKey());				
		if (commentReportEAO.findByAttributes(paramMap).size() > 0) {
			Utils.log("likeComment", "User already liked this comment !");
			this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "You can't like a comment twice!", FacesMessage.SEVERITY_WARN));
			this.getMessageHandler().dischargeMessages();
			
			commentBean2.setLikecount(commentBean2.getLikecount() - 1);
			
			return;
		}
		
		comment.setLikecount(commentBean2.getLikecount());
		commentEAO.update(comment);
		
		CommentReport commentReport = new CommentReport();
		commentReport.setComment(comment);
		commentReport.setUser(user);
		commentReport.setReporttype(Reporttype.Like);
		commentReportEAO.create(commentReport);
		
		this.getMessageHandler().addJSFMessage(new JSFMessage("Sucess", "You liked this commented successfully!"));
		this.getMessageHandler().dischargeMessages();
	}
	
	public void onCommentSpam(info.whitebyte.component.commentbox.Comment commentBean) {
		Comment comment = commentEAO.findById(Long.parseLong(commentBean.getId()));
		User user = userSession.getCurrentUser();
		
		if (user == null) {
			Utils.log("likeComment", "User is not logged in !");
			this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "You have to be logged in to report a comment as spam !", FacesMessage.SEVERITY_WARN));
			this.getMessageHandler().dischargeMessages();
			return;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("user", user);
	    paramMap.put("comment", comment);
	    paramMap.put("reporttype", Reporttype.Spam.getDbKey());				
		if (commentReportEAO.findByAttributes(paramMap).size() > 0) {
			Utils.log("likeComment", "User already reported this comment !");
			this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "You can't report a comment twice!", FacesMessage.SEVERITY_WARN));
			this.getMessageHandler().dischargeMessages();
			commentBean.setSpamcount(commentBean.getSpamcount() - 1);
			return;
		}
		
		comment.setSpamcount(comment.getSpamcount() + 1);
		commentEAO.update(comment);
		
		if (comment.getSpamcount() > Constants.MAX_SPAM_COMMENT_VOTES) {
			Article article = comment.getArticle();
			article.setCommentcount(article.getCommentcount() - 1);
			articleEAO.update(article);
			
			user = userEAO.update(user);
		}
		
		CommentReport commentReport = new CommentReport();
		commentReport.setComment(comment);
		commentReport.setUser(user);
		commentReport.setReporttype(Reporttype.Spam);
		commentReportEAO.create(commentReport);
		
		this.getMessageHandler().addJSFMessage(new JSFMessage("Success", "You reported the comment successfully as spam!"));
		this.getMessageHandler().dischargeMessages();
	}
	
	public boolean onCommentEdit(info.whitebyte.component.commentbox.Comment commentBean) {
		try {
			Comment newComment = commentEAO.findById(Long.parseLong(commentBean.getId()));

			if ((newComment.getUser().getId() != userSession.getUser_id()) && !userSession.isAdmin()) {
				Utils.persistantlog("HACKING ATTACK:", "Comment: " + newComment.getUser().getId() + " User: " + userSession.getUser_id());
				return false;
			}

			Date now = new Date(DateUtils.getNow());

			String stripped_html = StripHtml.StripTagsAndAttributes(commentBean.getComment_text(), Constants.ALLOWED_TAGS_COMMENTS);
			
			if (stripped_html.length() == 0) {
				this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "You can't submit empty comments.", FacesMessage.SEVERITY_WARN));
				return false;
			}

			newComment.setComment(stripped_html);
			newComment.setModification_time(now);

			this.commentEAO.update(newComment);
			
			this.getMessageHandler().addJSFMessage(new JSFMessage("Sucess", "You successfully edited the comment !"));
		} catch (Exception e) {
			this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", e.getMessage(), FacesMessage.SEVERITY_FATAL));
			Utils.persistantlog("editComment", e.getMessage());
		}
		
		this.getMessageHandler().dischargeMessages();
		return true;
	}
	
	public void onCommentDelete(info.whitebyte.component.commentbox.Comment commentBean) {
		try {
			Comment comment = commentEAO.findById(Long.parseLong(commentBean.getId()));			
			comment.setComment(commentBean.getComment_text());			
			commentEAO.update(comment);
			
			this.getMessageHandler().addJSFMessage(new JSFMessage("Sucess", "Comment was removed successfully."));
		} catch (Exception e) {
			this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", e.getMessage(), FacesMessage.SEVERITY_FATAL));
			Utils.persistantlog("removeComment", e.getMessage());
		}
		
		this.getMessageHandler().dischargeMessages();
	}
	
	public void onPageChange(Long newPage) {
		this.commentsPage = newPage;	
		fillComments();
		
		Utils.log("", "pages: " + this.comments.size());
	}
	
	public void vote(long article_id, boolean like, boolean isView) {
		Article article = userSession.vote(article_id, like, isView);
		
		this.getArticleBean().setLikecount(article.getTotallikecount());
		this.getArticleBean().setDislikecount(article.getDislikecount());
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

	public List<info.whitebyte.component.commentbox.Comment> getComments() {
		return comments;
	}

	public void setComments(List<info.whitebyte.component.commentbox.Comment> comments) {
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
