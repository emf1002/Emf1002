package mine.emf1002.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * 文件工具类
 * @author zhangshuaipeng
 *
 */
public class FileUtil {
	private static Logger logger=Logger.getLogger(FileUtil.class);
	/**
	 * 删除一个空目录
	 * @param dir
	 */
	public static void doDeleteEmptyDir(String dir){
		File f=new File(dir);
		f.delete();
	}
	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir){
		if(dir.isDirectory()){
			String[] childs=dir.list();
			for(int i=0;i<childs.length;i++){
				Boolean success=deleteDir(new File(dir,childs[i]));
				if(!success){
					return false;
				}
			}
		}
		return dir.delete();
	}
	/**
	 * 得到指定文件的文件对象，如果不存在则创建
	 * @param filePath
	 * @return
	 */
	public static File createFile(String filePath){
		File file=new File(filePath);
		try {
			if(!file.exists()){
					file.createNewFile();
			}
			return file;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("创建文件错误，错误信息"+e.getMessage());
			return null;
		}
	}
	/**
	 * 向指定的文件写入文本
	 * @param str
	 * @param file
	 */
	public  static void writeFileStr(String str,File file){
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter(file));
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("写入文本失败，失败信息"+e.getMessage());
		}
	}
}
