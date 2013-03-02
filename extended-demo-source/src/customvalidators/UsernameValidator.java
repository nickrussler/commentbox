package customvalidators;
 
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import utils.InjectHelper;
import db.eao.UserEAO;
import db.eao.local.UserEAOLocal;

@FacesValidator("customvalidators.UsernameValidator")
public class UsernameValidator implements Validator {
	
	UserEAOLocal userEAO = InjectHelper.lookup(UserEAO.class);
 
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {		
		String val = (String) value;
		
		if ((val == null) || (val.isEmpty())) {
			FacesMessage msg = new FacesMessage("Username", "Please choose an username!");		
			throw new ValidatorException(msg);
		}
		
		if (val.length() < 4) {
			FacesMessage msg = new FacesMessage("Username", "Min. Username length is 4 characters!");		
			throw new ValidatorException(msg);
		}
		
		if (val.length() > 28) {
			FacesMessage msg = new FacesMessage("Username", "Max. Username length is 28 characters!");		
			throw new ValidatorException(msg);
		}
		
		if (!val.matches("^[A-Za-z0-9_-şŞıİçÇöÖüÜĞğ]{1,999}$")) {
			FacesMessage msg = new FacesMessage("Username", "Username contains illegal characters (e.g. ;*#)!");		
			throw new ValidatorException(msg);
		}
		
		if (userEAO.findUserByUsername(val) != null) {
			FacesMessage msg = new FacesMessage("Username", "Username was already taken!");		
			throw new ValidatorException(msg);
		}
		
		if ((val == null) || (val.toLowerCase().equals("anonymous"))) {
			FacesMessage msg = new FacesMessage("Username", "This username is reserved!");		
			throw new ValidatorException(msg);
		}
	}
}