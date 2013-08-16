package db.eao;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import db.eao.local.BaseEAOLocal;

public class BaseEAO<T> implements BaseEAOLocal<T> {

	// unitName ist Optional
	@PersistenceContext(unitName = "commentboxDB")
	EntityManager em;

	protected String classname;
	protected Class<T> entityClass;
	
	public void initQuerys() {
		// Nothing to do here ?
	}

	@SuppressWarnings("unchecked")
	public BaseEAO() {
		try {

			ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
			// You may need this split or not, use logging to check

			String parameterClassName = pt.getActualTypeArguments()[0].toString().split("\\s")[1];

			// Instantiate the Parameter and initialize it.
			entityClass = (Class<T>) Class.forName(parameterClassName);

			classname = entityClass.getName().substring(entityClass.getName().lastIndexOf(".") + 1);

		} catch (ClassNotFoundException e) {
			Logger.getLogger(this.getClass().toString()).info(e.getMessage());
		}
	}

	public void create(T entity) {
		em.persist(entity);
	}

	public void delete(T entity) {
		// Merge is needed to get the entity into the managed state, otherwise
		// we can't remove it
		// Merge returns a managed copy of the parameter
		em.remove(em.merge(entity));
	}

	public T update(T entity) {
		return em.merge(entity);
	}

	public T findById(long id) {
		return em.find(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		String JPQL_SELECT = "select e from " + classname + " e ";
		Query q = em.createQuery(JPQL_SELECT);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(int maxResults, int firstResult) {

		CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		Query q = em.createQuery(cq);
		q.setMaxResults(maxResults);
		q.setFirstResult(firstResult);

		return q.getResultList();
	}

	/**
	 * Returns a list of Objects that match an attribute value
	 * 
	 * @param attribute
	 *            name of the bean property
	 * @param value
	 *            of the attribute
	 */
	public List<T> findByAttribute(String attribute, Object value) {
		try {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> c = cb.createQuery(entityClass);
			Root<T> rootObj = c.from(entityClass);
			List<T> objs = em.createQuery(c.select(rootObj).where(cb.equal(rootObj.get(attribute), value))).getResultList();

			return objs;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Returns a list of Objects that match a list of attributes values.
	 * Attributes can be compared to null by setting the name of a property and
	 * null as value.
	 * 
	 * @param attributes
	 *            map that contains as the key values the name of the bean
	 *            properties and as value the attribute value to be matched
	 */
	public List<T> findByAttributes(Map<String, Object> attributes) {
		return findByAttributes(attributes, null, null);
	}
	
	/**
	 * Returns a list of Objects that match a list of attributes values.
	 * Attributes can be compared to null by setting the name of a property and
	 * null as value.
	 * 
	 * @param attributes
	 *            map that contains as the key values the name of the bean
	 *            properties and as value the attribute value to be matched
	 */
	public List<T> findByAttributes(Map<String, Object> attributes, Integer maxResults, Integer firstResult) {
		try {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> c = cb.createQuery(entityClass);
			Root<T> rootObj = c.from(entityClass);
			CriteriaQuery<T> qry = c.select(rootObj);

			Predicate p = cb.conjunction();

			for (Entry<String, Object> entry : attributes.entrySet()) {

				if (entry.getValue() != null)
					p = cb.and(p, cb.equal(rootObj.get(entry.getKey()), entry.getValue()));
				else
					p = cb.and(p, cb.isNull(rootObj.get(entry.getKey())));
			}
			qry.where(p);
			TypedQuery<T> typedquery = em.createQuery(qry);
			
			if (maxResults != null) {
				typedquery.setMaxResults(maxResults);
			}
			
			if (firstResult != null) {
				typedquery.setFirstResult(firstResult);
			}
			
			List<T> objs = typedquery.getResultList();		

			return objs;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}