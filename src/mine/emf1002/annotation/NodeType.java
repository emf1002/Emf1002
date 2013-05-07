package mine.emf1002.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mine.emf1002.constant.TreeNodeType;


/**
 * 描述树形实体字段的注解
 * @author zhangshuaipeng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)  
public @interface NodeType {
	public TreeNodeType type(); //树形字段类型
}
