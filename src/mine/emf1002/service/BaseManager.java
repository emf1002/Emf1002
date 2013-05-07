package mine.emf1002.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import mine.emf1002.dao.IDao;
import mine.emf1002.utils.Page;

import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Component;

@Component("baseManager")
public class BaseManager implements Manager {
	
	@Resource(name="baseDao")
	private IDao baseDao;
	@Override
	public Object add(Object object) {
		
		return baseDao.add(object);
	}

	@Override
	public void delete(Object object) {
		baseDao.delete(object);

	}

	@Override
	public void deleteById(Class<?> clazz, Serializable id) {
		baseDao.deleteById(clazz, id);

	}
	@Override
	public void deleteByIds(Class<?> clazz,String pkName,String...ids){
		baseDao.deleteByIds(clazz, pkName,ids);
	}

	@Override
	public int deleteByCondition(Class<?> clazz, Map<String, Object> conditions) {
		return baseDao.deleteByCondition(clazz, conditions);
	}

	@Override
	public int deleteByCondition(Class<?> clazz, String whereHql) {
		return baseDao.deleteByCondition(clazz, whereHql);
	}

	@Override
	public Object update(Object object) {
		return baseDao.update(object);
	}

	@Override
	public long getCount(Class<?> clazz) {
		return baseDao.getCount(clazz);
	}

	@Override
	public long getCount(Class<?> clazz, String property, Object value) {
		return baseDao.getCount(clazz,property,value);
	}

	@Override
	public long getCount(Class<?> clazz, Map<String, Object> condition) {
		return baseDao.getCount(clazz,condition);
	}

	@Override
	public long getCount(Class<?> clazz, Criterion... criterions) {
		return baseDao.getCount(clazz,criterions);
	}

	@Override
	public long getCount(Class<?> clazz, String whereHql) {
		return baseDao.getCount(clazz, whereHql);
	}

	@Override
	public Object find(Class<?> clazz, Serializable id) {
		return baseDao.find(clazz, id);
	}

	@Override
	public Object findOnlyOne(Class<?> clazz, String property, Object value) {
	return baseDao.findOnlyOne(clazz, property,value);
	}

	@Override
	public Object findOnlyOne(Class<?> clazz, Criterion... criterions) {
		return baseDao.findOnlyOne(clazz, criterions);
	}

	@Override
	public Object findOnlyOne(Class<?> clazz, Map<String, Object> conditions) {
		return baseDao.findOnlyOne(clazz, conditions);
	}

	@Override
	public List<?> queryAll(Class<?> clazz) {
		return baseDao.queryAll(clazz);
	}

	@Override
	public List<?> queryByCondition(Class<?> clazz, String property,
			Object value) {
		return baseDao.queryByCondition(clazz, property, value);
	}

	@Override
	public List<?> queryByCondition(Class<?> clazz,
			Map<String, Object> conditions, String orderBy, boolean isAsc) {
		return baseDao.queryByCondition(clazz, conditions, orderBy, isAsc);
	}

	@Override
	public List<?> queryByCondition(Class<?> clazz, Criterion... criterions) {
		return baseDao.queryByCondition(clazz, criterions);
	}

	@Override
	public List<?> queryByWhereCondition(Class<?> clazz, String whereHql) {
		return baseDao.queryByWhereCondition(clazz, whereHql);
	}

	@Override
	public Page pageQuery(Class<?> clazz, int pageNo, int pageSize) {
		return baseDao.pageQuery(clazz, pageNo, pageSize);
	}

	@Override
	public Page pageQuery(Class<?> clazz, int pageNo, int pageSize,
			boolean isAsc, String orderBy) {
		return baseDao.pageQuery(clazz, pageNo, pageSize, isAsc, orderBy);
	}

	@Override
	public Page pageQuery(Class<?> clazz, int pageNo, int pageSize,
			Map<String, Object> conditions, boolean isAsc, String orderBy) {
		return baseDao.pageQuery(clazz, pageNo, pageSize, conditions, isAsc, orderBy);
	}

	@Override
	public Page pageQuery(Class<?> clazz, int pageNo, int pageSize,
			boolean isAsc, String orderBy, Criterion... criterions) {
		return baseDao.pageQuery(clazz, pageNo, pageSize, isAsc, orderBy, criterions);
	}

	@Override
	public Page pageQuery(Class<?> clazz, String whereHql, int pageNo,
			int pageSize, boolean isAsc, String orderBy) {
		return baseDao.pageQuery(clazz, whereHql, pageNo, pageSize, isAsc, orderBy);
	}

}
