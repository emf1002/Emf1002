package mine.emf1002.listener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * 静态化spring上文件工具类
 * @author zhangshuaipeng
 *
 */
@Component
public class SpringContextHolder implements ApplicationContextAware{
	private static ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext ac)
			throws BeansException {
		// TODO Auto-generated method stub
		applicationContext=ac;
	}
	/**
	 * 得到spring上下文对象
	 * @return
	 */
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	/**
	 * 得到spring注入的bean实例
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName){
		return (T)applicationContext.getBean(beanName);
	}
}
