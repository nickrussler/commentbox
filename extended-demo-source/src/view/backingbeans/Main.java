package view.backingbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import utils.Constants;
import utils.MessageHandler;
import view.formbeans.ArticleBean;

import com.github.slugify.Slugify;

import db.eao.local.ArticleEAOLocal;
import db.entity.Article;

@Named
@SessionScoped
public class Main implements Serializable {
	private static final long serialVersionUID = -3400746001864579077L;

	@Inject
	private UserSession userSession;

	@Inject
	private ArticleEAOLocal articleEAO;

	@PostConstruct
	public void init() {
		featuredArticles = new ArrayList<ArticleBean>();
		selectedArticles = new ArrayList<ArticleBean>();
	}

	private List<ArticleBean> featuredArticles = new ArrayList<ArticleBean>();
	private List<ArticleBean> selectedArticles = new ArrayList<ArticleBean>();

	private String popularity_select = "1";
	private String location_select = "1";
	private String category_select = "-1";

	private int currentpage = 1;

	private String current_articletype = null;

	private String generatePageLink(int page, String prefix) {
		String pagination = "<a href=\"javascript:void(0)\" onclick=\"$('#filterForm').attr('action', '/";

		if (current_articletype == null) {
			pagination += "commentbox/" + "projects";
		} else {
			pagination += "" + current_articletype;
		}
		String nofollow = (page < Constants.MAX_ALLOWED_PAGE_CRAWL_COUNT) ? "" : "rel=\"nofollow\"";
		pagination += "/" + page + "/');$('#filterForm').submit();\" " + nofollow + " >" + prefix + page + "</a>";

		return pagination;
	}

	public String generatePagination() {
		String pagination = "";

		if (currentpage == 1) {
			pagination += "<span class=\"selected\">Page 1</span>";
		} else {
			pagination += generatePageLink(1, "Page ");
		}

		if (currentpage > 4) {
			pagination += "<span class=\"dots\">...</span>";
		}

		if (currentpage == 1) {
			pagination += generatePageLink(2, "");
			pagination += generatePageLink(3, "");
			pagination += generatePageLink(4, "");
			pagination += generatePageLink(5, "");
			pagination += generatePageLink(6, "");

			return pagination;
		}

		if (currentpage == 2) {
			pagination += "<span class=\"selected\">" + 2 + "</span>";
			pagination += generatePageLink(3, "");
			pagination += generatePageLink(4, "");
			pagination += generatePageLink(5, "");
			pagination += generatePageLink(6, "");

			return pagination;
		}

		if (currentpage == 3) {
			pagination += generatePageLink(2, "");
			pagination += "<span class=\"selected\">" + 3 + "</span>";
			pagination += generatePageLink(4, "");
			pagination += generatePageLink(5, "");
			pagination += generatePageLink(6, "");

			return pagination;
		}

		pagination += generatePageLink(currentpage - 2, "");
		pagination += generatePageLink(currentpage - 1, "");
		pagination += "<span class=\"selected\">" + currentpage + "</span>";
		pagination += generatePageLink(currentpage + 1, "");
		pagination += generatePageLink(currentpage + 2, "");

		return pagination;
	}

	private MessageHandler getMessageHandler() {
		return userSession.getMessages()[Constants.MessageClassMain];
	}

	public void load(ComponentSystemEvent event) throws IOException {
		this.getMessageHandler().dischargeMessages();
		userSession.disChargeNavigationMessages();

		if (FacesContext.getCurrentInstance().isPostback()) {
			// if filterpost:
			if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("filterForm")) {
				initArticles();
			} else {
				if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("searchForm")) {
					currentpage = 1;
					initArticles();
				}
			}
			return;
		}

		initArticles();
	}

	public void vote(long article_id, boolean like, boolean isView) {
		Article article = userSession.vote(article_id, like, isView);

		// TODO: remove this, go without logging in to requests, and try to like
		// an article, there here we get nullpointer exception
		if (featuredArticles != null) {
			if (!featuredArticles.isEmpty()) {
				for (ArticleBean articleBean : this.getFeaturedArticles()) {
					if (articleBean.getArticle_id().equals(article_id)) {
						articleBean.setLikecount(article.getTotallikecount());
						articleBean.setDislikecount(article.getDislikecount());
					}
				}
			}
		}

		for (ArticleBean articleBean : this.getSelectedArticles()) {
			if (articleBean.getArticle_id().equals(article_id)) {
				articleBean.setLikecount(article.getTotallikecount());
				articleBean.setDislikecount(article.getDislikecount());
			}
		}
	}

	public void fillSelectedArticles() {
		boolean orderbycreation = (this.getPopularity_select().charAt(0) == '1');
		boolean orderbylikes = !orderbycreation;

		int articles_per_page = Constants.selected_per_page;

		List<Article> findByParams = articleEAO.findByParams(orderbylikes, orderbycreation, articles_per_page, articles_per_page * (currentpage - 1));
		selectedArticles = new ArrayList<ArticleBean>();
		for (Article article : findByParams) {
			ArticleBean artBean = new ArticleBean();

			artBean.setArticle_id(article.getId());
			artBean.setArticletext(article.getArticle());
			artBean.setWebsitelink(article.getWebsitelink());
			artBean.setLikecount(article.getTotallikecount());
			artBean.setPhotofilepath(article.getPicture_filename());
			artBean.setTitle(article.getTitle());
			artBean.setCommentcount(article.getCommentcount());
			artBean.setCreation_time(article.getCreation_time());

			String slugifiedURLTitle = Slugify.slugify(article.getTitle());

			if (slugifiedURLTitle.length() > 79) {
				slugifiedURLTitle = slugifiedURLTitle.substring(0, 80);
			}

			if (slugifiedURLTitle.isEmpty()) {
				slugifiedURLTitle = "-";
			} else {
				if (slugifiedURLTitle.endsWith("-")) {
					slugifiedURLTitle = slugifiedURLTitle.substring(0, slugifiedURLTitle.length() - 2);
				}
			}

			artBean.setTitle2(slugifiedURLTitle);

			selectedArticles.add(artBean);
		}
	}

	public void initArticles() {
		fillSelectedArticles();
	}

	public List<ArticleBean> getFeaturedArticles() {
		return featuredArticles;
	}

	public void setFeaturedArticles(List<ArticleBean> featuredArticles) {
		this.featuredArticles = featuredArticles;
	}

	public String getPopularity_select() {
		return popularity_select;
	}

	public void setPopularity_select(String popularity_select) {
		this.popularity_select = popularity_select;
	}

	public String getLocation_select() {
		return location_select;
	}

	public void setLocation_select(String location_select) {
		this.location_select = location_select;
	}

	public String getCategory_select() {
		return category_select;
	}

	public void setCategory_select(String category_select) {
		this.category_select = category_select;
	}

	public List<ArticleBean> getSelectedArticles() {
		return selectedArticles;
	}

	public void setSelectedArticles(List<ArticleBean> selectedArticles) {
		this.selectedArticles = selectedArticles;
	}

	public String getCurrent_articletype() {
		return current_articletype;
	}

	public void setCurrent_articletype(String current_articletype) {
		this.current_articletype = current_articletype;
	}

	public int getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}
}