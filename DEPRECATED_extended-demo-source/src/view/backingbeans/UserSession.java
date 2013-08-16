package view.backingbeans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import utils.Constants;
import utils.JSFMessage;
import utils.MessageHandler;
import utils.Utils;
import db.eao.local.ArticleEAOLocal;
import db.eao.local.ArticleReportEAOLocal;
import db.eao.local.UserEAOLocal;
import db.entity.Article;
import db.entity.ArticleReport;
import db.entity.User;
import db.enums.Reporttype;
import db.enums.Usertype;

@Named
@SessionScoped
public class UserSession implements Serializable {
	private static final long serialVersionUID = 1654109306326722706L;

	private MessageHandler[] messages = new MessageHandler[Constants.MessageClassCount];
	
	@Inject
	private UserEAOLocal userEAO;

	@Inject
	private ArticleEAOLocal articleEAO;

	@Inject
	private ArticleReportEAOLocal articleReportEAO;
	
	private String MAIN_WEBSITE_URL = Constants.MAIN_WEBSITE_URL;

	private String username;
	private String avatar_filename;
	private long user_id = -1;

	@PostConstruct
	public void init() {
		for (int i = 0; i < Constants.MessageClassCount; i++) {
			messages[i] = new MessageHandler();
		}
	}

	public long getUser_id() {
		return user_id;
	}
	
	public String getUser_idAsString() {		
		if (user_id == -1) {
			return "";
		}
		
		return user_id + "";
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public User getCurrentUser() {
		if (user_id == -1) {
			return null;
		}

		return userEAO.findById(user_id);
	}

	public void updateUser(User user) {
		if (user_id == -1) {
			return;
		}

		userEAO.update(user);
	}

	public boolean isLoggedIn() {
		return (this.user_id != -1);
	}

	public boolean isUser() {
		if (!isLoggedIn()) {
			return false;
		}

		User user = userEAO.findById(user_id);

		return (user.getUsertype().equals(Usertype.User));
	}

	public boolean isAdmin() {
		if (!isLoggedIn())
			return false;

		User user = userEAO.findById(user_id);

		return (user.getUsertype().equals(Usertype.Admin));
	}

	public String getMAIN_WEBSITE_URL() {
		return MAIN_WEBSITE_URL;
	}

	public void setMAIN_WEBSITE_URL(String mAIN_WEBSITE_URL) {
		MAIN_WEBSITE_URL = mAIN_WEBSITE_URL;
	}

	public Article vote(long article_id, boolean like, boolean isView) {
		Utils.log("vote", "Commence voting !");

		Reporttype reporttype = like ? Reporttype.Like : Reporttype.Dislike;
		Article article = this.articleEAO.findById(article_id);
		User user = this.getCurrentUser();
		User author = article.getCreator();

		if (user == null) {
			JSFMessage jsfMessage = new JSFMessage(null, "Error", "You need to be logged in to vote !", FacesMessage.SEVERITY_WARN);

			if (isView) {
				MessageHandler messageHandler = this.getMessages()[Constants.MessageClassArticleView];
				messageHandler.addJSFMessage(jsfMessage);
				messageHandler.dischargeMessages();
			} else {
				MessageHandler messageHandler = this.getMessages()[Constants.MessageClassMain];
				messageHandler.addJSFMessage(jsfMessage);
				messageHandler.dischargeMessages();
			}

			RequestContext context = RequestContext.getCurrentInstance();

			context.execute("registration_dialogVar.show();");

			Utils.log("vote", "The User who tried to vote is not logged in !");
			return article;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("user", user);
		paramMap.put("article", article);
		List<ArticleReport> duplicates = articleReportEAO.findByAttributes(paramMap);

		if (duplicates.size() > 0) {
			ArticleReport duplicate = duplicates.get(0);

			if (duplicate.getReporttype().equals(reporttype)) {
				JSFMessage jsfMessage = new JSFMessage(null, "Error", "You can't vote an article twice !", FacesMessage.SEVERITY_WARN);

				if (isView) {
					MessageHandler messageHandler = this.getMessages()[Constants.MessageClassArticleView];
					messageHandler.addJSFMessage(jsfMessage);
					messageHandler.dischargeMessages();
				} else {
					MessageHandler messageHandler = this.getMessages()[Constants.MessageClassMain];
					messageHandler.addJSFMessage(jsfMessage);
					messageHandler.dischargeMessages();
				}

				Utils.log("vote", "Already voted !");
				return article;
			}

			duplicate.setReporttype(reporttype);
			articleReportEAO.update(duplicate);

			if (like) {
				article.setLikecount(article.getLikecount() + 1);
				article.setDislikecount(article.getDislikecount() - 1);
			} else {
				article.setLikecount(article.getLikecount() - 1);
				article.setDislikecount(article.getDislikecount() + 1);
			}

			Utils.log("vote", "Vote found with different reporttype, updated the old one !");
		} else {
			ArticleReport articleReport = new ArticleReport();
			articleReport.setArticle(article);
			articleReport.setUser(user);
			articleReport.setReporttype(reporttype);
			articleReportEAO.create(articleReport);

			userEAO.update(user);

			if (like) {
				article.setLikecount(article.getLikecount() + 1);
			} else {
				article.setDislikecount(article.getDislikecount() + 1);
			}
		}

		if (author != null) {
			userEAO.update(author);
		}

		article.setTotallikecount(article.getLikecount() - article.getDislikecount());

		article = articleEAO.update(article);

		return article;
	}

	public void disChargeNavigationMessages() {
		this.getMessages()[Constants.MessageClassNavigation].dischargeMessages();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean hasMessageForClientId(String clientId) {
		return Utils.hasMessageForClientId(clientId);
	}

	public MessageHandler[] getMessages() {
		return messages;
	}

	public void setMessages(MessageHandler[] messages) {
		this.messages = messages;
	}

	public String getAvatar_filename() {
		return avatar_filename;
	}

	public void setAvatar_filename(String avatar_filename) {
		this.avatar_filename = avatar_filename;
	}
}
