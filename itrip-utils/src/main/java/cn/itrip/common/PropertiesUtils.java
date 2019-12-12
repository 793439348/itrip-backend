package cn.itrip.common;

import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @description: search项目专用，读取配置文件的工具类
 * @author: zeng
 * @createDate: 2019-12-10
 * @version: v1.0
 */
public class PropertiesUtils {
    private static Properties props;

    /**
     * 加载配置文件
     * @param fileName
     */
    private static void readProperties(String fileName) {
        props = new Properties();
        try {
            InputStreamReader inputStream = new InputStreamReader(PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName), "UTF-8");
            props.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据配置文件的key取value
     * @param fileName 文件名
     * @param key
     * @return
     */
    public static String get(String fileName, String key) {
        readProperties(fileName);
        return props.getProperty(key);
    }

}
