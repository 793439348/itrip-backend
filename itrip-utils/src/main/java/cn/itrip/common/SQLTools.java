package cn.itrip.common;

/**
 * @description: 防止sql注入 工具类
 * @author: zeng
 * @createDate: 2019-12-10
 * @version: v1.0
 */
public class SQLTools {

    /**
     * mybatis 模糊查询防止sql注入
     * @param keyword
     * @return
     */
    public static String transfer(String keyword) {
        if (keyword.contains("%") || keyword.contains("_")) {
            keyword = keyword.replaceAll("\\\\", "\\\\\\\\")
                  .replaceAll("\\%", "\\\\%")
                  .replaceAll("\\_", "\\\\_");
        }
        return keyword;
    }
}
