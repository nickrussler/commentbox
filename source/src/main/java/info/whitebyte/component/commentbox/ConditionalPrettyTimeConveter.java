package info.whitebyte.component.commentbox;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("info.whitebyte.ConditionalPrettyTimeConveter")
public class ConditionalPrettyTimeConveter implements Converter {
	private static Converter prettyTimeConverter = null;
	
	static {
		try {			
			prettyTimeConverter = FacesContext.getCurrentInstance().getApplication().createConverter("org.ocpsoft.PrettyTimeConverter");
		} catch (Exception e) {
			// Nothing to do here
		}
    }
	
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (prettyTimeConverter != null) {
			return prettyTimeConverter.getAsString(arg0, arg1, arg2);
		} else {
			// Fallback to simple method
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy");
			return sdf.format((Date) arg2);
		}
	}
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		throw new ConverterException("Does not yet support converting String to Date");
	}
}