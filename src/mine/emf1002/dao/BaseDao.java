package mine.emf1002.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import mine.emf1002.utils.Page;
import mine.emf1002.utils.StringUtil;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

@Component("baseDao")
public class BaseDao implements IDao {
	
	
	private Class<?> entityClass;
	private static Logger logger = Logger.getLogger(HibernateTemplate.class);
	
	@Resource
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public Object add(Object object) {
		 getSession().save(object);
		 return object;
	}

	@Override
	public void delete(Object object) {
		getSession().delete(object);
	}

	@Override
	public void deleteById(Class<?> clazz,Serializable id) {
		delete(find(clazz,id));
	}
	
	@Override
	public void deleteByIds(Class<?> clazz,String pkName,String...ids){
		String s =  StringUtil.join(ids, ',');
		String hql = createDelHqlStr(clazz) + pkName + " IN (" +s +");";
		Query query = this.getSession().createQuery(hql);
		query.executeUpdate();
	}

	@Override
	public int deleteByCondition(Class<?> clazz,Map<String, Object> conditions) {
		Query query = this.getSession().createQuery(createDelHqlStr(clazz)+createEqString(conditions));
		return query.executeUpdate();
	}

	@Override
	public int deleteByCondition(Class<?> clazz,String whereHql) {
		Query query = this.getSession().createQuery(createDelHqlStr(clazz)+" where " +whereHql);
		return query.executeUpdate();
	}

	@Override
	public Object update(Object object) {
		this.getSession().saveOrUpdate(object);
		return object;
	}
	
	/**
	 * 更新符合条件的数据为空
	 * 
	 * @param mapValue
	 * @param criterions
	 * @return
	 */
	public int updateNull(Class<?> clazz,String whereSql,String...properties) {
		StringBuilder sb = new StringBuilder("update " + clazz.getName()+" set ");
		for(String str:properties){
			sb.append(str+"= null,");
		}
		sb.deleteCharAt(sb.length()-1);
		if(null!=whereSql){
			sb.append(" where " + whereSql);
		}
		return getSession().createQuery(sb.toString()).executeUpdate();
	}

	@Override
	public long getCount(Class<?> clazz) {
		return (long) createCriteria(clazz).setProjection(Projections.rowCount())
				.list().get(0);
	}

	@Override
	public long getCount(Class<?> clazz,String property, Object value) {
		return (long) createCriteria(clazz).add(Restrictions.eq(property, value)).setProjection(Projections.rowCount())
				.list().get(0);
	}

	@Override
	public long getCount(Class<?> clazz,Map<String, Object> condition) {
		return (long) createEqualCriteria(clazz, condition).setProjection(Projections.rowCount())
				.list().get(0);
	}

	@Override
	public long getCount(Class<?> clazz,Criterion... criterions) {
		
		return (long)addCriterions(createCriteria(clazz),criterions).setProjection(Projections.rowCount())
				.list().get(0);
	}

	@Override
	public long getCount(Class<?> clazz,String whereHql) {
		String s = "select count(*) from " + clazz.getName() + " where "
				+ whereHql;
		Query query = getSession().createQuery(s);
		return (long) query.uniqueResult();
	
	}

	@Override
	public Object find(Class<?> clazz,Serializable id) {
		return getSession().get(clazz, id);
	}

	@Override
	public Object findOnlyOne(Class<?> clazz,String property, Object value) {
		return createCriteria(clazz).add(Restrictions.eq(property, value)).uniqueResult();
	}

	@Override
	public Object findOnlyOne(Class<?> clazz, Criterion... criterions) {
		Criteria criteria = this.getSession().createCriteria(clazz);
		for(Criterion criterion:criterions){
			criteria.add(criterion);
		}
		return criteria.uniqueResult();
	}

	@Override
	public Object findOnlyOne(Class<?> clazz, Map<String, Object> conditions) {
		return createEqualCriteria(clazz, conditions).uniqueResult();
	}
	
	public Object findOnlyOne(Class<?> clazz,String wherehql){
		String s = "from " + clazz.getName() + " " +wherehql;
		return this.createQuery(s).uniqueResult();
	}

	@Override
	public List<?> queryAll(Class<?> clazz) {
		return createCriteria(clazz).list();
	}

	@Override
	public List<?> queryByCondition(Class<?> clazz, String property,
			Object value) {
		return createCriteria(clazz).add(Restrictions.eq(property, value)).list();
	}

	@Override
	public List<?> queryByCondition(Class<?> clazz,
			Map<String, Object> conditions, String orderBy, boolean isAsc) {
		return createEqualCriteria(clazz, conditions).addOrder(isAsc ? Order.asc(orderBy) : Order.desc(orderBy)).list();
	}

	@Override
	public List<?> queryByCondition(Class<?> clazz, Criterion... criterions) {
		Criteria criteria = this.getSession().createCriteria(clazz);
		for(Criterion criterion:criterions){
			criteria.add(criterion);
		}
		return criteria.list();
	}

	@Override
	public List<?> queryByWhereCondition(Class<?> clazz, String whereHql) {
		return getSession().createQuery(addWhere(createSelectQueryStr(clazz), whereHql)).list();
	}

	@Override
	public Page pageQuery(Class<?> clazz, int pageNo, int pageSize) {
		return pageQuery(clazz, pageNo, pageSize, true, null);
	}

	@Override
	public Page pageQuery(Class<?> clazz, int pageNo, int pageSize,
			boolean isAsc, String orderBy) {
		return pageQuery(clazz, pageNo, pageSize,null, isAsc, orderBy);
	}

	@Override
	public Page pageQuery(Class<?> clazz, int pageNo, int pageSize,Map<String, Object> conditions,
			 boolean isAsc, String orderBy) {
		int start = Page.getStartOfPage(pageNo, pageSize);
		Criteria criteria = null;
		long count;
		if (null != conditions) {
			createEqualCriteria(clazz, conditions);
			count = getCount(clazz,conditions);
		} else {
			count = getCount(clazz);
			criteria = createCriteria(clazz);
		}
		if (null != orderBy) {
			addOrderBy(criteria, orderBy, isAsc);
		}
		List<?> l = criteria.setMaxResults(pageSize).setFirstResult(start).list();
		return new Page(start, count, pageSize, l);
	}

	@Override
	public Page pageQuery(Class<?> clazz, int pageNo, int pageSize,
			boolean isAsc, String orderBy, Criterion... criterions) {
		int start = Page.getStartOfPage(pageNo, pageSize);
		Criteria criteria = createCriteria(clazz);
		long count;
		if (null != criterions) {
			addCriterions(criteria, criterions);
			count = getCount(clazz,criterions);
		} else {
			count = getCount(clazz);
		}
		if (null != orderBy) {
			addOrderBy(criteria, orderBy, isAsc);
		}
		List<?> l = criteria.setMaxResults(pageSize).setFirstResult(start).list();
		return new Page(start, count, pageSize, l);
	}

	@Override
	public Page pageQuery(Class<?> clazz,String whereHql,int pageNo, int pageSize,
			boolean isAsc, String orderBy) {

		long count;
		int start = Page.getStartOfPage(pageNo, pageSize);
		String hql = createSelectQueryStr(clazz);
		if (null != whereHql) {
			hql = addWhere(hql, whereHql);
			count = getCount(clazz,whereHql);
		} else {
			count = getCount(clazz);
		}
		if (null != orderBy) {
			hql = addOrder(hql, orderBy, isAsc);
		}
		
		List<?> l = getSession().createQuery(hql).setMaxResults(pageSize)
				.setFirstResult(start).list();
		return new Page(start, count, pageSize, l);
	}
	
	/**
	 * 创建带有条件限制的
	 * @param clazz
	 * @param map
	 * @return
	 */
	private Criteria createEqualCriteria(Class<?> clazz,Map<String,Object> map){
		Criteria criteria = getSession().createCriteria(clazz);
		for(Map.Entry<String,Object> entry:map.entrySet()){
			criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
		}
		return criteria;
	}
	
	private String createEqString(Map<String,Object> map){
		StringBuilder sb = null;
		for(Map.Entry<String,Object> entry:map.entrySet()){
			if(null == sb){
				sb = new StringBuilder(" where " + entry.getKey()+"="+entry.getValue().toString());
			} else {
				sb.append(" and " + entry.getKey()+"="+entry.getValue().toString());
			}
		}
		return sb.toString();
	}
	
	private String createDelHqlStr(Class<?> clazz){
		return "DELETE FROM "+clazz.getName() +" WHERE "; 
	}
	
	/**
	 * 创建普通Criteria对象
	 * 
	 * @return
	 */
	private Criteria createCriteria(Class<?> clazz) {
		return getSession().createCriteria(clazz);
	}
	
	/**
	 * 创建不带条件的字符串
	 * @return
	 */
	private String createSelectQueryStr(Class<?> clazz) {
		return "from " + clazz.getName();
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
	
	private Query createQuery(String hql){
		return this.getSession().createQuery(hql);
	}
}
