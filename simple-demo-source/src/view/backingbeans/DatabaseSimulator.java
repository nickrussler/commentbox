package view.backingbeans;

import info.whitebyte.component.commentbox.Comment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import utils.SerializationUtils;

@ApplicationScoped
public class DatabaseSimulator implements Serializable {
	private static final long serialVersionUID = -4562076201196928335L;

	private List<Comment> comments = new ArrayList<Comment>();
	private int uniqueID = 0;

	public List<Comment> getComments(Long start, Long count) {
		int startInt = start.intValue();
		int endInt = start.intValue() + count.intValue();
		
		if (endInt > this.comments.size()) {
			endInt = this.comments.size();
		}
		
		return SerializationUtils.cloneObject(new ArrayList<Comment>(this.comments.subList(startInt, endInt)));
	}
	
	private Comment _findCommentByID(Comment parent, String commentid) {
		if (parent.getId().equals(commentid)) {
			return parent;
		}
		
		for (Comment answer : parent.getAnswers()) {
			Comment result = _findCommentByID(answer, commentid);
			
			if (result != null) {
				return result;
			}
		}
		
		return null;
	}	
	
	private Comment findCommentByID(String commentid) {
		for (Comment comment : comments) {
			Comment result = _findCommentByID(comment, commentid);
			
			if (result != null) {
				return result;
			}
		}
		
		return null;
	}
	
	public List<Comment> getAnswers(Comment comment) {
		return SerializationUtils.cloneObject(findCommentByID(comment.getId()).getAnswers());
	}
	
	public void createComment(Comment comment) {
		comment.setId(this.uniqueID + "");
		this.uniqueID++;
		
		this.comments.add(0, SerializationUtils.cloneObject(comment));
	}
	
	public void createAnswer(Comment parent, Comment comment) {
		comment.setId(this.uniqueID + "");
		this.uniqueID++;
		
		System.out.println(this.findCommentByID(parent.getId()).getAnswers().size());
		System.out.println("##############einmal!" + comment.getId());
		
		this.findCommentByID(parent.getId()).getAnswers().add(0, SerializationUtils.cloneObject(comment));
		
		System.out.println(this.findCommentByID(parent.getId()).getAnswers().size());
	}
	
	public void likeComment(Comment comment) {
		Comment databaseComment = findCommentByID(comment.getId());
		databaseComment.setLikecount(comment.getLikecount());
	}
	
	public void spamComment(Comment comment) {
		Comment databaseComment = findCommentByID(comment.getId());
		databaseComment.setSpamcount(comment.getSpamcount());
	}
	
	public void editComment(Comment comment) {
		Comment databaseComment = findCommentByID(comment.getId());
		databaseComment.setComment_text(comment.getComment_text());
	}
	
	public void deleteComment(Comment comment) {
		Comment databaseComment = findCommentByID(comment.getId());
		databaseComment.setComment_text(comment.getComment_text());
		databaseComment.setDeleted(true);
	}
}