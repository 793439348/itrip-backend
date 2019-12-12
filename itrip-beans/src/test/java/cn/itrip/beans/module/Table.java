package cn.itrip.beans.module;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:映射数据库 表的信息
 * @author: zeng
 * @createDate: 2019-12-11
 * @version: v1.0
 */
public class Table {
    private String tableName; // 表名
    private String className; // 类名

    List<Columns> columns = new ArrayList<Columns>(); // 集合：所有字段

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Columns> getColumns() {
        return columns;
    }

    public void setColumns(List<Columns> columns) {
        this.columns = columns;
    }
}
