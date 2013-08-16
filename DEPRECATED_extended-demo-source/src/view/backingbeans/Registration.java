package view.backingbeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import utils.Constants;
import utils.DateUtils;
import utils.JSFMessage;
import utils.MessageHandler;
import utils.Utils;
import view.formbeans.RegistrationBean;
import db.eao.local.UserEAOLocal;
import db.entity.User;
import db.enums.Usertype;

@ManagedBean
@ViewScoped
public class Registration implements Serializable {
	private static final long serialVersionUID = -4703014068698018453L;

	@Inject
	private UserEAOLocal userEAO;

	@Inject
	private UserSession userSession;

	private RegistrationBean registrationBean = new RegistrationBean();

	private MessageHandler getNavigationMessageHandler() {
		return userSession.getMessages()[Constants.MessageClassNavigation];
	}

	public void register() {
		User user = new User();

		user.setUsername(this.registrationBean.getUsername());
		user.setEmail(this.registrationBean.getEmail());
		user.setPassword_hash(Utils.createPasswordHash(this.registrationBean.getPassword()));
		user.setUsertype(Usertype.User);
		user.setCreation_time(DateUtils.getNowAsDate());
		user.setAvatar_filename(Constants.DEFAULT_AVATAR_FILENAME);

		userEAO.create(user);

		userSession.setUser_id(user.getId());
		userSession.setUsername(user.getUsername());
		userSession.setAvatar_filename(user.getAvatar_filename());
		getNavigationMessageHandler().addJSFMessage(new JSFMessage("Welcome", "Welcome to our Project Management Website, try out our new Comment Component ;)"));

		Utils.RedirectTo(Constants.HOMEPAGE);
	}

	public RegistrationBean getRegistrationBean() {
		return registrationBean;
	}

	public void setRegistrationBean(RegistrationBean registrationBean) {
		this.registrationBean = registrationBean;
	}
}
