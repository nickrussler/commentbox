package view.backingbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;

import org.primefaces.model.UploadedFile;

import utils.Constants;
import utils.DateUtils;
import utils.HashUtils;
import utils.ImageUploadUtils;
import utils.JSFMessage;
import utils.MessageHandler;
import utils.StripHtml;
import utils.Utils;
import view.formbeans.ArticleBean;
import db.eao.local.ArticleEAOLocal;
import db.eao.local.ArticleReportEAOLocal;
import db.eao.local.CommentEAOLocal;
import db.eao.local.CommentReportEAOLocal;
import db.entity.Article;
import db.entity.ArticleReport;
import db.entity.Comment;
import db.entity.CommentReport;
import db.entity.User;

@ManagedBean
@ViewScoped
public class ArticleEdit implements Serializable{
	private static final long serialVersionUID = 6086158632865397129L;

	@Inject
	private UserSession userSession;

	@Inject
	private ArticleEAOLocal articleEAO;
	
	@Inject
	private ArticleReportEAOLocal articleReportEAO;
	
	@Inject
	private CommentEAOLocal commentEAO;
	
	@Inject
	private CommentReportEAOLocal commentReportEAO;

	private ArticleBean articleEditBean = null;
	private long article_id = -1L;

	private UploadedFile uploadedFile;

	public void load(ComponentSystemEvent event) throws IOException {
		this.getMessageHandler().dischargeMessages();
		userSession.disChargeNavigationMessages();
		
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		
		articleEditBean = new ArticleBean();
		setUploadedFile(null);
		
		Article art = articleEAO.findById(article_id);

		if (isEdit()) {
			fillFormbean(art);

			if ((userSession.getCurrentUser() != null && ((art.getCreator() != null && (art.getCreator().getId() == userSession.getCurrentUser().getId())) || userSession.isAdmin()))) {
				// everything ok, this user can edit this article
			} else {
				if (!userSession.isLoggedIn()) {
					getArticleViewMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "You need to be logged in to edit an Article!", FacesMessage.SEVERITY_ERROR));
				} else {
					getArticleViewMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "You are not allowed to edit this Article!", FacesMessage.SEVERITY_ERROR));
				}

				Utils.RedirectTo(Constants.ARTICLE_VIEW+"?id="+art.getId()+"&"+Constants.RedirectWithoutQuestionmark,true);
				return;
			}
		} else {
			if (!userSession.isLoggedIn()) {
				getMainMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "You need to be logged in to create a Project", FacesMessage.SEVERITY_ERROR));
				Utils.RedirectTo(Constants.HOMEPAGE);
			}
			
			if (!isNew()) {
				getMainMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "This Project does not exist :/", FacesMessage.SEVERITY_ERROR));
				Utils.RedirectTo(Constants.HOMEPAGE);
			}			
		}
	}
	
	private MessageHandler getMessageHandler() {
		return userSession.getMessages()[Constants.MessageClassArticleEdit];
	}
	
	private MessageHandler getMainMessageHandler() {
		return userSession.getMessages()[Constants.MessageClassMain];
	}
	
	private MessageHandler getArticleViewMessageHandler() {
		return userSession.getMessages()[Constants.MessageClassArticleView];
	}

	private void fillFormbean(Article art) {
		if (isEdit()) {
			articleEditBean.setArticletext(art.getArticle());
			articleEditBean.setWebsitelink(art.getWebsitelink());
			articleEditBean.setDislikecount(art.getDislikecount());
			articleEditBean.setLikecount(art.getTotallikecount());
			articleEditBean.setPhotofilepath(art.getPicture_filename());
			articleEditBean.setPhotolink("");			
			articleEditBean.setTitle(art.getTitle());
			articleEditBean.setCreation_time(art.getCreation_time());
		}
	}
	
	public void save() {
		process();
	}

	public void create() {
		process();
	}
	
	public void process() {
		Article article =null;
		boolean isEdit=isEdit();
		boolean isNew=isNew();
		
		if(isEdit){
			article = articleEAO.findById(article_id);	
		}
		
		// var declaretions BEGIN
		String title = null;
		String articleText = null;
		String photolink = null;
		String websitelink = null;
		String uploadedImageFilename = null;
		// var declaretions END
	
		//To show in all cases: BEGIN
		title = articleEditBean.getTitle();
		if (title.length() < 10) {
			getMessageHandler().addJSFMessage(new JSFMessage("editArticle:title", "Error", "The title is too short (minimum: 10 characters)", FacesMessage.SEVERITY_ERROR));
		}
		if (title.length() > 200) {
			getMessageHandler().addJSFMessage(new JSFMessage("editArticle:title", "Error", "The title is too long (maximum: 200 characters)", FacesMessage.SEVERITY_ERROR));
		}

		articleText = articleEditBean.getArticletext();
		if (articleText.length() < 10) {
			getMessageHandler().addJSFMessage(new JSFMessage("editArticle:articletext", "Error", "The description is too short (minimum: 10 characters)", FacesMessage.SEVERITY_ERROR));
		}
		if (articleText.length() > 9000) {
			getMessageHandler().addJSFMessage(new JSFMessage("editArticle:articletext", "Error", "The description is too long (maximum: 9000 characters)", FacesMessage.SEVERITY_ERROR));
		}	
		
		photolink = articleEditBean.getPhotolink();				

		boolean image_wanted = false;
		if (uploadedFile != null) {
			Utils.log(this.getClass().toString(), "Upload Image");
			uploadedImageFilename = ImageUploadUtils.fromClient(uploadedFile,Constants.IMG_SIZE, "image");
			image_wanted = true;
		} else {
			if (photolink != null && photolink.length() > 0) {
				Utils.log(this.getClass().toString(), "Download Image");
				uploadedImageFilename = ImageUploadUtils.fromWeb(photolink, "editArticle:photolink", this.getMessageHandler(),Constants.IMG_SIZE, "image");
				image_wanted = true;
			}
		}
		if (image_wanted) {
			if (uploadedImageFilename == null) {
				getMessageHandler().addJSFMessage(new JSFMessage("editArticle:photolink", "Error", "There was an error loading your image", FacesMessage.SEVERITY_ERROR));
				getMessageHandler().addJSFMessage(new JSFMessage("editArticle:uploadedImage", "Error", "There was an error loading your image", FacesMessage.SEVERITY_ERROR));
			}
		}
		
		websitelink = articleEditBean.getWebsitelink();

		if (websitelink != null && websitelink.length() > 0) {
			if (!websitelink.substring(0, 10).toLowerCase().startsWith("http://") && !websitelink.substring(0, 10).toLowerCase().startsWith("https://")) {
				websitelink = "http://" + websitelink;
			}
		}		 
		
		if (!getMessageHandler().getMessages().isEmpty()) {
			getMessageHandler().dischargeMessages();
		} else {
			User user = userSession.getCurrentUser();
			if(isNew){
				article = new Article();
			}			

			try {
				Date now=DateUtils.getNowAsDate();
				if(isNew){
					article.setCreator(user);
					article.setCreation_time(now);
					article.setEditingpassword(HashUtils.generateVerificationCode());
				}				
						
				article.setTitle(title);
				
				if (uploadedImageFilename != null && uploadedImageFilename.length() > 1) {
					article.setPicture_filename(uploadedImageFilename);
				}
				
				String stripped_html = articleText;
				
				if (!userSession.isAdmin()) {				
					stripped_html = StripHtml.StripTagsAndAttributes(stripped_html, Constants.ALLOWED_TAGS_ARTICLE);
				}
				
				article.setArticle(stripped_html);
				article.setWebsitelink(websitelink);				
				
				if(isNew){
					articleEAO.create(article);				
					getArticleViewMessageHandler().addJSFMessage(new JSFMessage(null, "Success", "Your project was created successfully.", FacesMessage.SEVERITY_INFO));
				}
				if(isEdit){
					articleEAO.update(article);
					getArticleViewMessageHandler().addJSFMessage(new JSFMessage(null, "Success", "Your project was edited successfully.", FacesMessage.SEVERITY_INFO));
				}
				
				Utils.RedirectTo(Constants.ARTICLE_VIEW+"?id="+article.getId()+"&"+Constants.RedirectWithoutQuestionmark,true);
			} catch (Exception e) {
				Utils.persistantlog("ArticleEdit", "Error Creating /Editing Article: Exception: " + e.getMessage() + ", User-Id: " + ((user == null) ? "Not logged in" : user.getId() + ""));
				getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "Something awful happened here, please go away, there is nothing to see..", FacesMessage.SEVERITY_ERROR));
				getMessageHandler().dischargeMessages();
			}
		}
	}

	public void cancel() throws IOException {
		String temp = " editing Project.";
		if (isNew()) {
			temp = " creating Project.";
		}

		getMainMessageHandler().addJSFMessage(new JSFMessage(null, "Bilgi", "You canceled " + temp, FacesMessage.SEVERITY_INFO));
		Utils.RedirectTo(Constants.HOMEPAGE);
		return;
	}

	public void deleteHelper(){
		Article art=articleEAO.findById(article_id);
		
		List<Comment> comments = commentEAO.findByAttribute("article", art);
		
		for (Comment comment : comments) {
			List<CommentReport> commentReports = commentReportEAO.findByAttribute("comment", comment);
			
			
			for (CommentReport commentReport : commentReports) {
				commentReportEAO.delete(commentReport);							
			}
			commentEAO.delete(comment);
		}
		
		List<ArticleReport> articleReports = articleReportEAO.findByAttribute("article",art);
		
		for (ArticleReport articleReport : articleReports) {
			articleReportEAO.delete(articleReport);
		}					
		
		articleEAO.delete(art);		
	}

	public void delete2(){
		if((userSession.getUser_id() != articleEAO.findById(article_id).getCreator().getId()) && (!userSession.isAdmin())){
			this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "You can't delete this article.", FacesMessage.SEVERITY_ERROR));
		} else {			
			deleteHelper();
			
			getMainMessageHandler().addJSFMessage(new JSFMessage(null,"Delete Article", "Article was deleted successfully.", FacesMessage.SEVERITY_INFO));
			Utils.RedirectTo(Constants.HOMEPAGE);
			return;
		}
	}
	
	public boolean isNew() {
		return article_id == -1L;
	}

	public boolean isEdit() {
		return articleEAO.findById(article_id) != null;
	}

	public ArticleBean getArticleEditBean() {
		return articleEditBean;
	}

	public void setArticleEditBean(ArticleBean articleEditBean) {
		this.articleEditBean = articleEditBean;
	}	

	public long getArticle_id() {
		return article_id;
	}

	public void setArticle_id(long article_id) {
		this.article_id = article_id;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
}