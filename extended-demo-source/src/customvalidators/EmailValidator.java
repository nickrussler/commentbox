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

@FacesValidator("customvalidators.EmailValidator")
public class EmailValidator implements Validator {
	
	UserEAOLocal userEAO = InjectHelper.lookup(UserEAO.class);
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {		
		String val = (String) value;
		
		if ((val == null) || (val.isEmpty())) {
			FacesMessage msg = new FacesMessage("Email", "Please enter an email!");		
			throw new ValidatorException(msg);
		}
		
		if (val.length() > 100) {
			FacesMessage msg = new FacesMessage("Email", "This email address is way too long!");		
			throw new ValidatorException(msg);
		}
		
		if (!val.matches(EMAIL_PATTERN)) {
			FacesMessage msg = new FacesMessage("Email", "Please enter a correct email!");		
			throw new ValidatorException(msg);
		}
		
		if (userEAO.findByAttribute("email", val).size() > 0) {
			FacesMessage msg = new FacesMessage("Email", "This email is already associated with another account!");	
			throw new ValidatorException(msg);
		}
	}
}