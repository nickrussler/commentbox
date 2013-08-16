package customvalidators;
 
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.model.DefaultUploadedFile;

import utils.Constants;
import utils.Utils;
 
@FacesValidator("customvalidators.UploadValidator")
public class UploadValidator implements Validator {
 
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {		
		DefaultUploadedFile file = (DefaultUploadedFile)value;
		
		if (file == null) {
			return;
		}
		
		if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png") && !file.getContentType().equals("image/gif")) {
			String exception_msg = file.getFileName() + " was blocked because it's no image !";
			
			Utils.log("UploadValidator", exception_msg);
			
			FacesMessage msg = new FacesMessage("Upload failed", exception_msg);
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ValidatorException(msg);
		}
		
		if (file.getSize() > Constants.MAX_FILE_SIZE_IMAGE_UPLOAD) {
			String exception_msg = file.getFileName() + " was blocked because it too large !";
			
			Utils.log("UploadValidator", exception_msg);
			
			FacesMessage msg = new FacesMessage("Upload failed", exception_msg);
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ValidatorException(msg);
		}
	}
}