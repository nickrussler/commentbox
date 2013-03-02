package utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

public class Utils {

	public static void addGeneralInfoMessage(String target, FacesMessage msg) {
		FacesContext.getCurrentInstance().addMessage(target, msg);
	}
	
	public static void addGeneralInfoMessage(FacesMessage msg) {
		addGeneralInfoMessage(null, msg);
	}

	public static void addGeneralInfoMessage(String target, String title, String message) {
		addGeneralInfoMessage(target, new FacesMessage(FacesMessage.SEVERITY_INFO, title, message));
	}

	public static void addMessageForException(String target, String title, String message) {
		addGeneralInfoMessage(target, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message));
	}

	public static void log(String source, String msg) {
		Logger.getLogger(source).info(msg);
	}

	public static void persistantlog(String source, String msg) {
		Logger.getLogger(source).info(msg);
	}

	public static void RedirectTo(String adress) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			NavigationHandler navigator = context.getApplication().getNavigationHandler();
			navigator.handleNavigation(context,null,adress+"?faces-redirect=true");
			
			//the old, non-intelligent way
			//FacesContext.getCurrentInstance().getExternalContext().redirect(adress);
		} catch (Exception e) {
			Logger.getLogger("Utils:RedirectTo: " + e.getMessage());
		}
	}
	public static void RedirectTo(String adress,boolean manuelRedirect) {
		try {
			if(!manuelRedirect){
					RedirectTo(adress);
				return;
			}
			FacesContext context = FacesContext.getCurrentInstance();
			NavigationHandler navigator = context.getApplication().getNavigationHandler();
			navigator.handleNavigation(context,null,adress);
			
			//the old, non-intelligent way
			//FacesContext.getCurrentInstance().getExternalContext().redirect(adress);
		} catch (Exception e) {
			Logger.getLogger("Utils:RedirectTo: " + e.getMessage());
		}
	}
	
	public static String createPasswordHash(String password){
		return HashUtils.md5WithSalt(password);
	}
	
	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

	public static String toSlug(String input) {
		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");
		return slug.toLowerCase(Locale.ENGLISH);
	}
	
    public static Boolean hasMessageForClientId(String clientId) {
        FacesContext context = FacesContext.getCurrentInstance();
        final Iterator<String> clientIdIterator = context.getClientIdsWithMessages();
        while (clientIdIterator.hasNext()) {
        	String next = clientIdIterator.next();
        	
        	if (next != null) {        	
	        	if (next.equals(clientId)) {
	        		return true;
	            }
        	}
        }
        return false;
    }
}