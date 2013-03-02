package utils;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

public class JSFMessage {
	public String target = null;
	public String title;
	public String message;
	public Severity severity = FacesMessage.SEVERITY_INFO;
	
	private FacesMessage facesMessage;
	
	public JSFMessage(String title, String message) {
		this.title = title;
		this.message = message;
		
		this.facesMessage = new FacesMessage(severity, title, message);
	}
	
	public JSFMessage(String target, String title, String message) {
		this.target = target;
		this.title = title;
		this.message = message;
		
		this.facesMessage = new FacesMessage(severity, title, message);
	}

	public JSFMessage(String target, String title, String message, Severity severity) {
		this.target = target;
		this.title = title;
		this.message = message;
		this.severity = severity;
		
		this.facesMessage = new FacesMessage(severity, title, message);
	}
	
	public FacesMessage getFacesMessage() {
		return facesMessage;
	}
}
