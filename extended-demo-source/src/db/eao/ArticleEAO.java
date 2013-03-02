package db.eao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import db.eao.local.ArticleEAOLocal;
import db.entity.Article;

@Stateless
public class ArticleEAO extends BaseEAO<Article> implements ArticleEAOLocal {
	@Override
	public List<Article> findByParams(boolean orderbylikes, boolean orderbycreation, Integer maxResults, Integer firstResult) {
		StringBuilder JPQL_SELECT = new StringBuilder();

		JPQL_SELECT.append("select a from Article a where 1=1");

		if (orderbycreation == true) {
			JPQL_SELECT.append(" order by a.creation_time DESC");
		} else {
			if (orderbylikes == true) {
				JPQL_SELECT.append(" order by a.totallikecount DESC");
			}
		}

		TypedQuery<Article> typedQuery = em.createQuery(JPQL_SELECT.toString(), Article.class);

		if (maxResults != null) {
			typedQuery.setMaxResults(maxResults);
		}

		if (firstResult != null) {
			typedQuery.setFirstResult(firstResult);
		}

		return typedQuery.getResultList();
	}
}
