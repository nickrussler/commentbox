package utils;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

public class MessageHandler {

	private List<JSFMessage> messages = new ArrayList<JSFMessage>();

	public void addJSFMessage(JSFMessage message) {
		messages.add(message);
	}

	public void addJSFMessages(List<JSFMessage> messages) {
		this.messages.addAll(messages);
	}

	public void dischargeMessages() {
		for (JSFMessage message : messages) {
			FacesContext.getCurrentInstance().addMessage(message.target, message.getFacesMessage());
		}
		
		messages.clear();
	}
	
	public List<JSFMessage> getMessages() {
		return new ArrayList<JSFMessage>(messages);
	}
}