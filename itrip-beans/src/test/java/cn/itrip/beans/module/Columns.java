package cn.itrip.beans.module;

/**
 * @description: 映射数据库表的字段信息
 * @author: zeng
 * @createDate: 2019-12-11
 * @version: v1.0
 */
public class Columns {

    private String columnsName; //列名称
    private String fileName; // 属性名称
    private String upperFiledName; //属性名称的首字母大小（用于拼接get/set方法名）
    private String columnType; // 列的类型
    private String javaType; // java的类型
    private String remarks; // 列注释

    public String getColumnsName() {
        return columnsName;
    }

    public void setColumnsName(String columnsName) {
        this.columnsName = columnsName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUpperFiledName() {
        return upperFiledName;
    }

    public void setUpperFiledName(String upperFiledName) {
        this.upperFiledName = upperFiledName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
