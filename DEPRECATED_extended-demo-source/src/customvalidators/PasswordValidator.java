package customvalidators;
 
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("customvalidators.PasswordValidator")
public class PasswordValidator implements Validator {
 
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {		
		String val = (String) value;
		
		UIInput otherInput = (UIInput) context.getViewRoot().findComponent("register_form:register_password2");
	    String otherValue = (String) otherInput.getSubmittedValue();
	    		
		if ((val == null) || (val.isEmpty())) {
			FacesMessage msg = new FacesMessage("Password", "Please enter a Password!");		
			throw new ValidatorException(msg);
		}
		
		if (val.length() < 4) {
			FacesMessage msg = new FacesMessage("Password", "Min. Password length is 4 characters!");		
			throw new ValidatorException(msg);
		}
		
		if (val.length() > 50) {
			FacesMessage msg = new FacesMessage("Password", "Max. Password length is 50 characters!");		
			throw new ValidatorException(msg);
		}
		
		if (!val.equals(otherValue)) {
			FacesMessage msg = new FacesMessage("Password", "Passwords don't match");		
			throw new ValidatorException(msg);
		}
	}
}