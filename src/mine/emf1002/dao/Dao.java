package mine.emf1002.dao;

import java.io.Serializable;
import java.util.List;

import mine.emf1002.utils.Page;

public interface Dao<T> {
	
	/**
	 * 取记录总数
	 * @return
	 */
	public long getCount();
	
	/**
	 * 按条件取总数
	 * @param condition
	 * @return
	 */
	//public long findCount(Map<String, Object> condition);

	
	/**
	 * 往数据库插入一条数据
	 * @param object
	 * @return
	 */
	public Serializable add(T object);
	
	/**
	 * 删除一个已经被持久化的对象，该对象类型为T
	 * 
	 * @param transientObject
	 *            需要删除的持久化对象
	 */
	public boolean delete (T transientObject);

	/**
	 * 根据对象id删除一个对象，该对象类型为T
	 * 
	 * @param id
	 *            需要删除的对象的id
	 */
	public boolean deleteById(Serializable id);
	
	/**
	 * 批量删除
	 * @param conditionTable
	 */
	//public boolean deleteByCondition(Hashtable<String,Object> conditionTable);
	
	/**
	 * 修改数据库中一条数据
	 * @param object
	 * @return 是否修改成功
	 */
	public boolean update(T object);
	
	/**
	 * @param columnName
	 * @param param
	 * @param condition
	 * @return
	 */
	//public boolean updateByEqualCondition(Map<String,Object> mapValue,Map<String, Object> condition);
	
	
	/**
	 * 根据Id查找一个类型为T的对象。
	 * 
	 * @param id
	 *            传入的Id的值
	 * @return 一个类型为T的对象
	 */
	public T find(Serializable id);

	/**
	 * 获取对象的全部集合，集合中的对象为T
	 * 
	 * @return 一组T对象的List集合
	 */
	public List<T> queryAll();
	
	/**
	 * 查询带排序
	 */
	//public List<T> findAllByOrder(String propertyName,String orderBy,boolean isAsc);
	
	/**
	 * 根据条件查询 带排序
	 * @param columnName
	 * @param param
	 * @param condition
	 * @return
	 */
	//public List<T> findByCondition(Map<String,Object> conditionMap,String orderBy,boolean isAsc);
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page pageQuery(int pageNo,int pageSize);
	
	/**
	 * 分页查询，带排序
	 * @param conditionTable
	 * @param pageNo
	 * @param pageSize
	 * @param isAsc
	 * @return
	 */
	public Page pageQuery(int pageNo,int pageSize,boolean isAsc,String orderBy);
	
	
	
	
	
}
