package mine.emf1002.action;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mine.emf1002.model.BaseEntity;
import mine.emf1002.rbac.model.Department;
import mine.emf1002.rbac.model.EndUser;
import mine.emf1002.security.SecurityUserHolder;
import mine.emf1002.service.Manager;
import mine.emf1002.utils.DateUtil;
import mine.emf1002.utils.JsonUtil;
import mine.emf1002.utils.ModelUtil;
import mine.emf1002.utils.Page;
import mine.emf1002.utils.StringUtil;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 通用的基础Action类封装
 * 
 * @author zhangshuaipeng
 * 
 */
public abstract class BaseAction extends ActionSupport implements
		ModelDriven<Object>, ServletResponseAware, ServletRequestAware,
		Serializable {
	private static final long serialVersionUID = -620965937227955611L;
	/** 日志输出对象 */
	private static Logger logger = Logger.getLogger(BaseAction.class);
	/** 请求对象 */
	protected HttpServletRequest request;
	/** 返回对象 */
	protected HttpServletResponse response;
	/** 项目部署的WebRoot路径 */
	public static String webrootAbsPath;
	/** 项目部署的class路径 */
	public static String absClassPath;
	/** Json工具类 */
	// protected static JsonBuilder jsonBuilder;
	/** 服务层 */
	@Resource(name = "baseManager")
	protected Manager baseManager;
	/** ------------变量声明------------- */
	/** 实体全名称 */
	protected String modelName;
	/** 上传文件字段值 */
	protected String uploadFields;
	/** 表名 */
	protected String tableName;
	/** 主键名 */
	protected String pkName;
	/** 主键值 */
	protected String pkValue;
	/** 第几页 */
	protected int start = 0;
	/** 每页几条 */
	protected int limit = 30;
	/** 排序 */
	protected String sort;
	/** 排序字段 */
	protected String orderBy;
	/** 是否升序 */
	protected boolean isAsc;
	/** 查询条件 */
	protected String whereSql = "";
	/** 主功能过滤条件 */
	protected String parentSql = "";
	protected String querySql = "";
	/** 排序条件 */
	protected String orderSql = "";
	/** 主键值列表 */
	protected String[] ids;
	/** 传输字符串 */
	protected String strData;
	/** 外键 **/
	protected String foreignKey;
	/** 为了json排除的字段 */
	protected String excludes = ""; // checked
	protected Boolean expanded;
	protected String node;
	protected String nodeId;

	/** ------------开始封装通用方法------------------ */

	/**
	 * 默认的保存方法
	 */
	public void doSave() {

		Object entity = getModel();
		try {

			if (entity instanceof BaseEntity) {
				buildModelCreateInfo((BaseEntity) entity);
			} else {
				logger.error("实体信息获取错误");
				toWrite(JsonUtil.returnFailureJson("'传入的实体信息错误'"));
				return;
			}

			entity = baseManager.add(entity);
			toWrite(JsonUtil.returnSuccessJson(JsonUtil.getJson(entity)));
		} catch (Exception e) {
			logger.error("保存方法出错，错误信息" + e.getMessage());
			toWrite(JsonUtil.returnFailureJson("'保存方法出错，错误信息" + e.getMessage()
					+ "'"));
		}
	}

	/**
	 * 默认的删除方法
	 */
	public void doRemove() {
		Object entity = getModel();
		try {
			if (ids.length > 0) {
				if (StringUtil.isEmpty(pkName)) {
					pkName = ModelUtil.getClassPkName(entity.getClass());
				}
				if (StringUtil.isEmpty(pkName)) {
					toWrite(JsonUtil.returnFailureJson("'得到类主键名失败'"));
					return;
				}
				baseManager.deleteByIds(entity.getClass(), pkName, ids);
				toWrite(JsonUtil
						.returnSuccessJson("'" + ids.length + "条记录被删除'"));
			} else {
				toWrite(JsonUtil.returnFailureJson("'传入ids为空'"));
			}
		} catch (Exception e) {
			logger.error("删除失败，失败信息:" + e.getMessage());
			toWrite(JsonUtil.returnFailureJson("'删除失败，失败信息:" + e.getMessage()
					+ "'"));
		}
	}

	/**
	 * 默认的更新
	 */
	public void doUpdate() {

		Object entity = getModel();
		try {
			if (entity instanceof BaseEntity) {
				buildModelModifyInfo((BaseEntity) entity);
			} else {
				logger.error("实体信息获取错误");
				toWrite(JsonUtil.returnFailureJson("'传入的实体信息错误'"));
				return;
			}
			entity = baseManager.update(entity);
			toWrite(JsonUtil.returnSuccessJson(JsonUtil.getJson(entity)));
		} catch (Exception e) {
			logger.error("更新方法出错，错误信息" + e.getMessage());
			toWrite(JsonUtil.returnFailureJson("'更新方法出错，错误信息" + e.getMessage()
					+ "'"));
		}
	}

	/**
	 * 默认的读取方法
	 */
	public void pageLoad() {
		Object entity = getModel();
		Page page = baseManager.pageQuery(entity.getClass(), whereSql, start,
				limit, isAsc, orderBy);
		toWrite(JsonUtil.returnPageJson(page));
	}

	/**
	 * 默认的表格的更新方法
	 */
	public void doUpdateList() {
	}

	/**
	 * 根据实体主键值获取实体信息
	 */
	public void getInfoById() {

		Object entity = getModel();
		try {
			if (StringUtil.isEmpty(pkValue)) {
				toWrite(JsonUtil.returnFailureJson("'得到类主键值失败'"));
				return;
			}
			entity = baseManager.find(entity.getClass(), pkValue);
			toWrite(JsonUtil.returnSuccessJson(JsonUtil.getJson(entity)));
		} catch (Exception e) {
			logger.error("获取实体信息失败，错误信息" + e.getMessage());
			toWrite(JsonUtil.returnFailureJson("'获取实体信息失败，错误信息"
					+ e.getMessage() + "'"));
		}
	}

	/**
	 * 默认的读取树形方法
	 */
	public void getTree() {
	}

	protected void toWrite(String contents) {
		if (ModelUtil.isNotNull(response)) {
			response.setContentType("text/html;charset=UTF-8;");
			Writer writer = null;
			try {
				response.setCharacterEncoding("UTF-8");
				writer = response.getWriter();
				writer.write(contents);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					writer.flush();
					writer.close();
					response.flushBuffer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 构建创建的实体信息
	 * 
	 * @param model
	 */
	protected void buildModelCreateInfo(BaseEntity entity) {

		// 登录人信息获取
		EndUser currentUser = SecurityUserHolder.getCurrentUser();
		Department currentDept = SecurityUserHolder.getCurrentDept();
		Date now = new Date();
		String dateStr = DateUtil.formatDateTime(now);
		entity.setCreateUser(currentUser.getUserCode());
		entity.setCreateUserName(currentUser.getUsername());
		entity.setCreateDept(currentDept.getDeptCode());
		entity.setCreateDeptName(currentDept.getDeptName());
		entity.setCreateTime(dateStr);
		if (entity.getOrderIndex() == null) {
			entity.setOrderIndex(0);
		}

	}

	/**
	 * 构建修改的实体信息
	 * 
	 * @param model
	 */
	protected void buildModelModifyInfo(BaseEntity entity) {

		EndUser currentUser = SecurityUserHolder.getCurrentUser();
		Department currentDept = SecurityUserHolder.getCurrentDept();
		Date now = new Date();
		String dateStr = DateUtil.formatDateTime(now);
		entity.setModifyUser(currentUser.getUserCode());
		entity.setModifyUserName(currentUser.getUsername());
		entity.setModifyDept(currentDept.getDeptCode());
		entity.setModifyDeptName(currentDept.getDeptName());
		entity.setModifyTime(dateStr);
		if (entity.getOrderIndex() == null) {
			entity.setOrderIndex(0);
		}
	}

	/** ------------结束封装通用方法------------------ */
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getUploadFields() {
		return uploadFields;
	}

	public void setUploadFields(String uploadFields) {
		this.uploadFields = uploadFields;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public String getPkValue() {
		return pkValue;
	}

	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getWhereSql() {
		return whereSql;
	}

	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}

	public String getOrderSql() {
		return orderSql;
	}

	public void setOrderSql(String orderSql) {
		this.orderSql = orderSql;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getStrData() {
		return strData;
	}

	public void setStrData(String strData) {
		this.strData = strData;
	}

	public String getExcludes() {
		return excludes;
	}

	public void setExcludes(String excludes) {
		this.excludes = excludes;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

	public String getParentSql() {
		return parentSql;
	}

	public void setParentSql(String parentSql) {
		this.parentSql = parentSql;
	}

	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

}
