package db.eao.local;

import java.util.List;
import java.util.Map;

public interface BaseEAOLocal<T> {
	public void create(T entity);

	public void delete(T entity);

	public T update(T entity);

	public T findById(long id);

	public List<T> findAll();

	public List<T> findAll(int maxResults, int firstResult);

	public List<T> findByAttribute(String attribute, Object value);

	public List<T> findByAttributes(Map<String, Object> attributes);
	
	public List<T> findByAttributes(Map<String, Object> attributes, Integer maxResults, Integer firstResult);
}