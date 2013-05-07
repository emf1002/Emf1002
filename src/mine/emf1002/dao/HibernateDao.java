package mine.emf1002.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import mine.emf1002.utils.GenericUtils;
import mine.emf1002.utils.Page;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class HibernateDao<T> implements Dao<T> {

	protected Class<?> entityClass;

	private SessionFactory sessionFactory;

	public HibernateDao() {
		/**
		 * this.getClass()的目的是返回当前对象运行时的类 通过工具类GenericUtils返回泛型T的实际类对象
		 */
		entityClass = GenericUtils.getSuperClassGenericType(getClass());
	}

	public Session getSession() {
		return sessionFactory.openSession();
	}

	@Override
	public Serializable add(T object) {
		return getSession().save(object);
	}

	@Override
	public boolean delete(T transientObject) {
		getSession().delete(transientObject);
		return true;
	}

	@Override
	public boolean deleteById(Serializable id) {
		delete(find(id));
		return true;
	}

	/**
	 * 根据where条件字符串进行删除 " name='name'"
	 * 
	 * @param whereSqls
	 * @return
	 */
	public int deleteByCondition(List<String> whereSqls) {
		return creatDeleteQuery(whereSqls).executeUpdate();
	}

	/**
	 * 根据where条件字符串进行删除 " name='name'"
	 * 
	 * @param whereSqls
	 * @return
	 */
	public int deleteByCondition(String whereSqls) {
		return createDeleteQuery(whereSqls).executeUpdate();
	}

	@Override
	public boolean update(T object) {
		getSession().update(object);
		return true;
	}

	/**
	 * 更新符合条件的数据
	 * 
	 * @param mapValue
	 * @param criterions
	 * @return
	 */
	public boolean updateByEqualCondition(Map<String, Object> mapValue,
			String whereSql) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getCount() {
		return (long) createCriteria().setProjection(Projections.rowCount())
				.list().get(0);
	}

	/**
	 * 查询
	 * 
	 * @param condition
	 * @return
	 */
	public long getCount(Map<String, Object> condition) {
		return (long) createCriteria(condition)
				.setProjection(Projections.rowCount()).list().get(0);
	}

	/**
	 * 查询符合条件的总数
	 * 
	 * @param criterions
	 * @return
	 */
	public long getCount(Criterion... criterions) {
		return (long) addCriterions(createCriteria(), criterions)
				.setProjection(Projections.rowCount()).list().get(0);
	}

	/**
	 * 增加排序
	 * 
	 * @param whereSql
	 * @return
	 */
	public long getCount(String whereSql) {
		String s = "select count(*) from " + entityClass.getName() + " where "
				+ whereSql;
		Query query = getSession().createQuery(s);
		return (long) query.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T find(Serializable id) {
		return (T) getSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public T findOnlyOne(String property, Object value) {
		String s = property + " = ?";
		return (T) createSelectQuery(s).setParameter(0, value).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public T findOnlyOne(String whereHql, Object... values) {
		return (T) createQuery(whereHql, values).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> queryAll() {
		return createCriteria().list();

	}

	@SuppressWarnings("unchecked")
	public List<T> queryByEqualCondition(Map<String, Object> conditionMap,
			String orderBy, boolean isAsc) {
		return createCriteria().add(Restrictions.allEq(conditionMap)).addOrder(
				isAsc ? Order.asc(orderBy) : Order.desc(orderBy)).list();
	}

	@SuppressWarnings("unchecked")
	public List<T> queryByWhereCondition(String whereSqls) {
		return createSelectQuery(whereSqls).list();
	}

	public List<T> queryByPropertyValue(String property, Object value) {
		return createCriteria().add(Restrictions.eq(property, value)).list();
	}

	@Override
	public Page pageQuery(int pageNo, int pageSize) {

		return pageQuery(pageNo, pageSize, true, null);
	}

	@Override
	public Page pageQuery(int pageNo, int pageSize, boolean isAsc,
			String orderBy) {
		return pageQuery(pageNo, pageSize, null, isAsc, orderBy);
	}

	/**
	 * 排序+条件 分页 通过sql条件
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param isAsc
	 * @param orderBy
	 * @param whereSql
	 * @return
	 */
	public Page pageQuery(int pageNo, int pageSize, String whereSql,
			boolean isAsc, String orderBy) {
		long count;
		int start = Page.getStartOfPage(pageNo, pageSize);
		String hql = createSelectQueryStr();
		if (null != whereSql) {
			hql = addWhere(hql, whereSql);
			count = getCount(whereSql);
		} else {
			count = getCount();
		}
		if (null != orderBy) {
			hql = addOrder(hql, orderBy, isAsc);
		}
		@SuppressWarnings("rawtypes")
		List l = getSession().createQuery(hql).setMaxResults(pageSize)
				.setFirstResult(start).list();
		return new Page(start, count, pageSize, l);
	}

	/**
	 * 排序+条件 分页 通过Criterion条件
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param isAsc
	 * @param orderBy
	 * @param criterions
	 * @return
	 */
	public Page pageQuery(int pageNo, int pageSize, boolean isAsc,
			String orderBy, Criterion... criterions) {
		int start = Page.getStartOfPage(pageNo, pageSize);
		Criteria criteria = createCriteria();
		long count;
		if (null != criterions) {
			addCriterions(criteria, criterions);
			count = getCount(criterions);
		} else {
			count = getCount();
		}
		if (null != orderBy) {
			addOrderBy(criteria, orderBy, isAsc);
		}
		@SuppressWarnings("rawtypes")
		List l = criteria.setMaxResults(pageSize).setFirstResult(start).list();
		return new Page(start, count, pageSize, l);
	}

	/**
	 * 创建普通Criteria对象
	 * 
	 * @return
	 */
	private Criteria createCriteria() {
		return getSession().createCriteria(entityClass);
	}
	
	/**
	 * 根据条件Map生成Criteria
	 * 
	 * @param conditionMap
	 * @return
	 */
	private Criteria createCriteria(Map<String, Object> conditionMap) {
		Criteria c = createCriteria();
		for (Map.Entry<String, Object> entry : conditionMap.entrySet()) {
			c.add(Restrictions.eq(entry.getKey(), entry.getValue()));
		}
		return c;
	}

	/**
	 * 创建Criteria对象，带有查询条件
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param criterions
	 * @return
	 */
	private Criteria addCriterions(Criteria criteria, Criterion... criterions) {
		if (null != criterions) {
			for (Criterion c : criterions) {
				criteria.add(c);
			}
		}
		return criteria;
	}

	/**
	 * 创建Criteria对象，带有查询条件，有排序功能。
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param orderBy
	 * @param isAsc
	 * @param criterions
	 * @return
	 */
	private Criteria addOrderBy(Criteria criteria, String orderBy, boolean isAsc) {
		if (null != orderBy) {
			criteria.addOrder(isAsc ? Order.asc(orderBy) : Order.desc(orderBy));
		}
		return criteria;
	}
	
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 创建delete Query
	 * 
	 * @param whereSqls
	 * @return
	 */
	private Query creatDeleteQuery(List<String> whereSqls) {
		StringBuilder sb = new StringBuilder("delete " + entityClass.getName()
				+ " where ");
		for (Iterator<String> it = whereSqls.iterator(); it.hasNext();) {
			sb.append(it.next()).append(it.hasNext() ? " and " : "");
		}
		Query query = getSession().createQuery(sb.toString());
		return query;
	}

	/**
	 * 创建删除Query
	 * 
	 * @param whereSqls
	 * @return
	 */
	private Query createDeleteQuery(String whereSqls) {
		String s = "delete " + entityClass.getName() + " where " + whereSqls;

		Query query = getSession().createQuery(s);
		return query;
	}
	
	/**
	 * 创建不带条件的字符串
	 * @return
	 */
	private String createSelectQueryStr() {
		return "from " + entityClass.getName();
	}
	
	/**
	 * 创建带where条件的字符串
	 * @param hql
	 * @param whereSql
	 * @return
	 */
	private String addWhere(String hql, String whereSql) {
		return hql + " where " + whereSql;
	}
	
	/**
	 * 创建带where条件和排序的字符串
	 * @param hql
	 * @param orderBy
	 * @param isAsc
	 * @return
	 */
	private String addOrder(String hql, String orderBy, boolean isAsc) {
		return hql + " order by " + orderBy + (isAsc ? " asc" : " desc");
	}

	/**
	 * 创建查询Query
	 * 
	 * @param whereSqls
	 * @return
	 */
	private Query createSelectQuery(String whereSql) {
		String s = "from " + entityClass.getName() + " where " + whereSql;
		return getSession().createQuery(s);

	}

	/**
	 * 
	 * @param hql
	 * @param values
	 * @return
	 */
	private Query createQuery(final String whereSql, final Object... values) {
		Query query = createSelectQuery(whereSql);
		for (int i = 0, j = values.length; i < j; i++) {
			query.setParameter(i, values[i]);
		}
		return query;

	}

	/**
	 * 给Query增加排序
	 * 
	 * @param query
	 * @param isAsc
	 * @param orderBy
	 * @return
	 */
	private Query addOrderBy(Query query, boolean isAsc, String orderBy) {
		return getSession().createQuery(
				query.getQueryString() + " order by " + orderBy
						+ (isAsc ? " asc" : " desc"));
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	

}
