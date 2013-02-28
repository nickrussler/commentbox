package view.backingbeans;

import info.whitebyte.component.commentbox.Comment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DatabaseSimulator implements Serializable {
	private static final long serialVersionUID = -4562076201196928335L;

	private List<Comment> comments = new ArrayList<Comment>();
	private int uniqueID = 0;

	public List<Comment> getComments() {
		return this.comments;
		
//		try {
//			return getListCopy();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		
//		return null;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

//	@SuppressWarnings("unchecked")
//	public List<Comment> getListCopy() throws Exception {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ObjectOutput out = null;
//		try {
//			out = new ObjectOutputStream(bos);
//			out.writeObject(comments);
//			byte[] yourBytes = bos.toByteArray();
//
//			ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
//			ObjectInput in = null;
//			try {
//				in = new ObjectInputStream(bis);
//				Object o = in.readObject();
//
//				return (List<Comment>) o;
//			} finally {
//				bis.close();
//				in.close();
//			}
//
//		} finally {
//			out.close();
//			bos.close();
//		}
//	}

	public int getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(int uniqueID) {
		this.uniqueID = uniqueID;
	}
}