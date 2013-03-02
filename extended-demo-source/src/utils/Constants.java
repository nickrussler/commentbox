package utils;

import java.net.URI;
import java.net.URISyntaxException;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class Constants {
		
	public static String Redirect="?faces-redirect=true";
	public static String RedirectWithoutQuestionmark="faces-redirect=true";
	
	public static long days_1 = 1L*24L*60L*60L*1000L; // 1 Days
	
	public static long days_4 = 4L*24L*60L*60L*1000L; // 4 Days
	
	public static long days_7 = 7L*24L*60L*60L*1000L; // 7 Days
	
	public static long days_90 = 90L*24L*60L*60L*1000L; // 90 Days
	
	public static int REMEMBER_ME_EXPIRATION_TIME = 2*7*24*60*60; // two weeks
	
	public static String HOMEPAGE="main";
	public static String ARTICLE_EDIT="article_edit";
	public static String ARTICLE_VIEW="article_view";
	public static String PROFILE="profile";

	public static final int DEPLOYMENT_VERSION = 5;

	public static String MAIN_WEBSITE_URL="/commentbox-demo/";	

	public static String getApplicationUri() {
		  try {
		    FacesContext ctxt = FacesContext.getCurrentInstance();
		    ExternalContext ext = ctxt.getExternalContext();
		    URI uri = new URI(ext.getRequestScheme(),
		          null, ext.getRequestServerName(), ext.getRequestServerPort(),
		          ext.getRequestContextPath(), null, null);
		    return uri.toASCIIString();
		  } catch (URISyntaxException e) {
		    throw new FacesException(e);
		  }
		}
	
	public static long MAX_FILE_SIZE_IMAGE_UPLOAD = 2L*1024L*1024L; // 2 MB
	
	public static final int IMG_SIZE = 150;
	public static final int IMG_AVATAR_SIZE = 70;
	
	public static String IMAGE_BASE_DIR = System.getProperty("user.home") + "/app-root/runtime/repo/uploadedImages/";
	
	public static String DEFAULT_AVATAR_FILENAME="default_avatar.png";
	
	
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static final int COMMENTS_PER_PAGE_COUNT = 5;
	
	public static final String[] ALLOWED_TAGS_ARTICLE = new String[] {"div", "span", "table", "p", "td", "tr", "ul", "ol", "li", "strong", "b", "strike", "u", "br", "em", "a"};
	
	public static final String[] ALLOWED_TAGS_COMMENTS = ALLOWED_TAGS_ARTICLE;
	
	public static final int MAX_SPAM_VOTES = 15;
	public static final int MAX_SPAM_COMMENT_VOTES = 15;
	
	public static final int MAX_EXPIRED_VOTES = 15;
	
	public static final long IMAGE_CACHE_EXPIRATION_TIME = 7L*24L*60L*60L*1000L;
	
	public static final byte MessageClassCount = 5;
	
	public static final byte MessageClassArticleEdit= 0;
	public static final byte MessageClassArticleView = 1;
	public static final byte MessageClassMain = 2;
	public static final byte MessageClassNavigation = 3;
	public static final byte MessageClassProfile = 4;
	
	
	public static final int featured_per_page = 0;
	public static final int selected_per_page = 12;
	
	public static final int MAX_ALLOWED_PAGE_CRAWL_COUNT = 10;
}
