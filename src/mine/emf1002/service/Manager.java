package mine.emf1002.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import mine.emf1002.utils.Page;

import org.hibernate.criterion.Criterion;

public interface Manager {

	/**
	 * 插入一条数据
	 * @param object
	 * @return
	 */
	public Object add(Object object);
	
	/**
	 * 删除一条数据
	 * @param object
	 * @return
	 */
	public void delete(Object object);
	
	/**
	 * 根据ID删除一条数据
	 * @param id
	 * @return
	 */
	public void deleteById(Class<?> clazz,Serializable id);
	
	/**
	 * 根据多个ID删除多条数据
	 * @param id
	 * @return
	 */
	public void deleteByIds(Class<?> clazz,String pkName,String...ids);

	/**
	 * 根据where条件字符串进行删除 " name='name'"
	 * 
	 * @param whereSqls "="连接的
	 * @return
	 */
	public int deleteByCondition(Class<?> clazz,Map<String,Object> conditions);
	
	/**
	 * 根据where条件字符串进行删除 " name='name'"
	 * 
	 * @param whereSqls
	 * @return
	 */
	public int deleteByCondition(Class<?> clazz,String whereHql);
	
	/**
	 * 更新一条数据
	 * @param object
	 * @return
	 */
	public Object update(Object object);
	
	/**
	 * 取得总记录数
	 * @return
	 */
	public long getCount(Class<?> clazz);
	
	
	/**
	 * 取得总记录数,通过一对属性和值
	 * @return
	 */
	public long getCount(Class<?> clazz,String property,Object value);

	/**
	 * 根据多个相等条件
	 * @param condition
	 * @return
	 */
	public long getCount(Class<?> clazz,Map<String, Object> condition);

	/**
	 * 查询符合条件的总数，通过Criterion
	 * 
	 * @param criterions
	 * @return
	 */
	public long getCount(Class<?> clazz,Criterion... criterions);

	/**
	 * 取得总数通过 hql条件，不包含where
	 * @param whereSql
	 * @return
	 */
	public long getCount(Class<?> clazz,String whereHql);
	
	/**
	 * 根据ID查询一条记录
	 * @param id
	 * @return
	 */
	public Object find(Class<?> clazz,Serializable id);
	
	/**
	 * 查询一条唯一记录，通过一对属性和值
	 * @param property 属性名
	 * @param value 属性值
	 * @return
	 */
	public Object findOnlyOne(Class<?> clazz,String property, Object value);
	
	/**
	 * 根据条件查询一条唯一记录，通过Criterion
	 * @param whereHql
	 * @param values
	 * @return
	 */
	public Object findOnlyOne(Class<?> clazz,Criterion... criterions);
	
	
	/**
	 * 根据条件查询一条唯一记录，通过多个相等的属性和值
	 * @param whereHql
	 * @param values
	 * @return
	 */
	public Object findOnlyOne(Class<?> clazz,Map<String,Object> conditions);
	
	/**
	 * 查询全部记录
	 * @return
	 */
	public List<?> queryAll(Class<?> clazz);
	
	/**
	 * 根据条件返回带排序的记录，一对属性和值
	 * @param conditionMap
	 * @param orderBy
	 * @param isAsc
	 * @return
	 */
	public List<?> queryByCondition(Class<?> clazz,String property, Object value);
	
	
	/**
	 * 根据条件返回带排序的记录，通过多个相等的属性和值
	 * @param conditionMap
	 * @param orderBy
	 * @param isAsc
	 * @return
	 */
	public List<?> queryByCondition(Class<?> clazz,Map<String,Object> conditions,
			String orderBy, boolean isAsc);
	
	/**
	 * 根据条件返回带排序的记录，通过Criterion
	 * @param conditionMap
	 * @param orderBy
	 * @param isAsc
	 * @return
	 */
	public List<?> queryByCondition(Class<?> clazz,Criterion... criterions);
	
	/**
	 * 根据条件返回记录列表，通过Hql，不含where
	 * @param whereSqls
	 * @return
	 */
	public List<?> queryByWhereCondition(Class<?> clazz,String whereHql);
	
	
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page pageQuery(Class<?> clazz,int pageNo, int pageSize);

	/**
	 * 带排序的分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param isAsc
	 * @param orderBy
	 * @return
	 */
	public Page pageQuery(Class<?> clazz,int pageNo, int pageSize, boolean isAsc,
			String orderBy);

	/**
	 * 排序+条件 分页 通过多个相等的条件
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param isAsc
	 * @param orderBy
	 * @param whereSql
	 * @return
	 */
	public Page pageQuery(Class<?> clazz,int pageNo, int pageSize, Map<String,Object> conditions,
			boolean isAsc, String orderBy);

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
	public Page pageQuery(Class<?> clazz,int pageNo, int pageSize, boolean isAsc,
			String orderBy, Criterion... criterions);
	
	/**
	 * 根据HQL分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param isAsc
	 * @param orderBy
	 * @param whereHql
	 * @return
	 */
	public Page pageQuery(Class<?> clazz, String whereHql,int pageNo, int pageSize, boolean isAsc,
			String orderBy);
	

}
