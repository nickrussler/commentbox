package db.eao.local;

import java.util.List;

import javax.ejb.Local;

import db.entity.Article;

@Local
public interface ArticleEAOLocal extends BaseEAOLocal<Article> {
	public List<Article> findByParams(boolean orderbylikes, boolean orderbycreation, Integer maxResults, Integer firstResult);
}