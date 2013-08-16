package view.backingbeans;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import utils.Constants;
import utils.JSFMessage;
import utils.MessageHandler;
import utils.Utils;
import view.formbeans.LoginBean;
import db.eao.local.UserEAOLocal;
import db.entity.User;

@ManagedBean
@ViewScoped
public class Navigation implements Serializable {
	private static final long serialVersionUID = -3400746001864579077L;
	
	@Inject
	private UserSession userSession;
	
	@Inject
	private UserEAOLocal userEAO;
	
	private LoginBean loginBean= new LoginBean();
	
	private MessageHandler getMessageHandler() {
		return userSession.getMessages()[Constants.MessageClassNavigation];
	}
	
	public void login() {
		Map<String, Object> paramMap=new HashMap<String, Object>();
	    paramMap.put("username", loginBean.getUsername());
	    paramMap.put("password_hash", Utils.createPasswordHash(loginBean.getPassword()));
		List<User> list= userEAO.findByAttributes(paramMap);
		
		// Let the user login with email
		if (list.isEmpty()) {
			paramMap=new HashMap<String, Object>();
		    paramMap.put("email", loginBean.getUsername());
		    paramMap.put("password_hash", Utils.createPasswordHash(loginBean.getPassword()));
			list = userEAO.findByAttributes(paramMap);
		}
		
		if(!list.isEmpty()){
			User user = list.get(0);
			
			userSession.setUser_id(user.getId());
			userSession.setUsername(user.getUsername());
			userSession.setAvatar_filename(user.getAvatar_filename());
			this.getMessageHandler().addJSFMessage(new JSFMessage("Welcome", "You successfully logged in !"));		
		} else {
			this.getMessageHandler().addJSFMessage(new JSFMessage(null, "Error", "Login failed !", FacesMessage.SEVERITY_ERROR));
		}
		this.getMessageHandler().dischargeMessages();
	}
	
	public void logout() {
		FacesContext facesContext = FacesContext.getCurrentInstance();			
		Cookie rememberMe = new Cookie("remember", "");
		rememberMe.setMaxAge(0);		
		((HttpServletResponse)facesContext.getExternalContext().getResponse()).addCookie(rememberMe);
		
		User user = userSession.getCurrentUser();
		userEAO.update(user);
		
		userSession.setUser_id(-1L);
		userSession.setUsername("");
		userSession.setAvatar_filename(null);
		Utils.RedirectTo(Constants.HOMEPAGE);
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}
	
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
}