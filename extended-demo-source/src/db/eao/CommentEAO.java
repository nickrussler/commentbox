package db.eao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import utils.Constants;

import db.eao.local.CommentEAOLocal;
import db.entity.Article;
import db.entity.Comment;

@Stateless
public class CommentEAO extends BaseEAO<Comment> implements CommentEAOLocal {

	@Override
	public List<Comment> findRootCommentsByArticleWithMaxSpamCount(Article article, int maxResults, int firstResult) {			
		String JPQL_SELECT_FIND_BY_ARTICLE = "select c from Comment c where (c.answered IS NULL) AND c.article.id = :articleid AND c.spamcount <= :maxspamcount order by c.creation_time DESC";
		TypedQuery<Comment> typedQuery = em.createQuery(JPQL_SELECT_FIND_BY_ARTICLE, Comment.class);
		
		typedQuery.setParameter("articleid", article.getId());
		typedQuery.setParameter("maxspamcount", Constants.MAX_SPAM_COMMENT_VOTES);
		
		if (maxResults > 0) {
			typedQuery.setMaxResults(maxResults);
		}
		
		typedQuery.setFirstResult(firstResult);

		return typedQuery.getResultList();
	}

	@Override
	public List<Comment> findByArticleWithMaxSpamCount(Article article) {
		return findRootCommentsByArticleWithMaxSpamCount(article, 0, 0);
	}
	
	@Override
	public List<Comment> findByParentWithMaxSpamCount(Comment parent) {
		String JPQL_SELECT_FIND_BY_ARTICLE = "select c from Comment c where (c.answered IS :parent) AND c.spamcount <= :maxspamcount order by c.creation_time DESC";
		TypedQuery<Comment> typedQuery = em.createQuery(JPQL_SELECT_FIND_BY_ARTICLE, Comment.class);
		
		typedQuery.setParameter("parent", parent);
		typedQuery.setParameter("maxspamcount", Constants.MAX_SPAM_COMMENT_VOTES);

		return typedQuery.getResultList();
	}
}