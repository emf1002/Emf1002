package mine.emf1002.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import mine.emf1002.constant.ExtFieldType;
import mine.emf1002.constant.StringVeriable;
import mine.emf1002.model.ExtFieldVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**    
 * 处理json的工具类，负责json数据转换成java对象和java对象转换成json    
 *     
 * @author yongtree    
 * @date 2008-11-22 上午10:47:13    
 * @version 1.0    
 */     
public class JsonUtil {      
     
    /**    
     * 将java对象转换成json字符串    
     *     
     * @param javaObj    
     * @return    
     */     
    public static String getJson(Object javaObj) {      
     
        JSONObject json;      
        json = JSONObject.fromObject(javaObj);      
        return json.toString();      
     
    }  
    
	/**
	 * 为操作成功返回Json
	 * @param strData
	 * @return
	 */
	public static String returnSuccessJson(String strData){
		StringBuffer returnJson = new StringBuffer("{ success : true, msg : ");
		returnJson.append(strData);
		returnJson.append("}");
		return returnJson.toString();
	}
	/**
	 * 为操作失败返回Json
	 * @param strData
	 * @return
	 */
	public static String returnFailureJson(String strData){
		StringBuffer returnJson = new StringBuffer("{ success : false, msg : ");
		returnJson.append(strData);
		returnJson.append("}");
		return returnJson.toString();
	}
	
	/**
	 * 返回分页对象的JSON字符串
	 * @param page 分页对象
	 * @return
	 */
	public static  String returnPageJson(Page page){
		StringBuilder sb = new StringBuilder("{");
		sb.append("totalCount:" + page.getTotalCount());
		return sb.toString();
	}
     
	
	/**
	 * 构建类的ExtJs的fields字段数据
	 * @param modelName
	 * @param fields
	 * @param excludes
	 * @return
	 */
	public static String getModelFileds(String modelName,Field[] fields,String excludes){
		List<ExtFieldVo> lists=new ArrayList<ExtFieldVo>();
		for(Field f:fields){
			String[] excludeArray=excludes.split(StringVeriable.STR_SPLIT);
			Boolean flag=false;
			for(String exclude:excludeArray){
				if(f.equals(exclude)){
					flag=true;
					break;
				}
			}
			if(flag){
				continue;
			}
			String fieldType=f.getType().getSimpleName().toLowerCase();
			Boolean excludeFlag=false;
			if(fieldType.equals("double")){
				fieldType=ExtFieldType.FLOAT;
			}else if(fieldType.equals("long")){
				fieldType=ExtFieldType.INT;				
			}else if(fieldType.equals("bigdecimal")){
				fieldType=ExtFieldType.INT;				
			}else if(fieldType.equals("timestamp")){
				fieldType=ExtFieldType.STRING;				
			}else if(fieldType.equals("date")){
				fieldType=ExtFieldType.STRING;				
			}else if(fieldType.equals("integer")){
				fieldType=ExtFieldType.INT;				
			}else if(fieldType.equals("string")){
				fieldType=ExtFieldType.STRING;
			}else{
				excludeFlag=true;
			}
			ExtFieldVo vo=new ExtFieldVo(f.getName(), fieldType);
			if(!excludeFlag){
				lists.add(vo);
			}
		}
		JSONArray jsonArray=JSONArray.fromObject(lists);
		String strData=jsonArray.toString();
		ModelUtil.modelJson.put(modelName, strData);
		return strData;
	}
} 



