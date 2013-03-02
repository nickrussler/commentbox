package db.eao.local;

import java.util.List;

import db.entity.Article;
import db.entity.Comment;

public interface CommentEAOLocal extends BaseEAOLocal<Comment> {
	List<Comment> findRootCommentsByArticleWithMaxSpamCount(Article article, int maxResults, int firstResult);
	List<Comment> findByArticleWithMaxSpamCount(Article article);
	List<Comment> findByParentWithMaxSpamCount(Comment parent);
}