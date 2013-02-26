package org.primefaces.extensions.component.commentbox;

public class CommentWrapper {
	private boolean dummy;
	private Comment comment;
	
	public CommentWrapper(boolean dummy, Comment comment) {
		this.dummy = dummy;
		this.comment = comment;
	}
	
	public boolean isDummy() {
		return this.dummy;
	}
	
	public void setDummy(boolean dummy) {
		this.dummy = dummy;
	}	
	
	public Comment getComment() {
		return this.comment;
	}
	
	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
