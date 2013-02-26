package view.backingbeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class UserSessionSimulator implements Serializable {
	private static final long serialVersionUID = 6280291891349291454L;
	
	private String userid = "" + (int) (Math.random()*10000d);

	public String getUserid() {
		return userid;
	}
}
