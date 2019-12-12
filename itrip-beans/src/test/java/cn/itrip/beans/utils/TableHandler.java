package cn.itrip.beans.utils;//package cn.itrip.beans.utils;
//
//import cn.itrip.beans.module.Columns;
//import cn.itrip.beans.module.Table;
//import cn.itrip.common.PropertiesUtils;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @description:连接数据库，获取数据库中所有表名，字段信息
// * @author: zeng
// * @createDate: 2019-12-11
// * @version: v1.0
// */
//public class TableHandler {
//    private static String DBDRIVER = PropertiesUtils.get("database.properties", "driver");
//
//    private static String DBURL = PropertiesUtils.get("database.properties", "url");
//
//    private static String DBUSER = PropertiesUtils.get("database.properties", "user");
//
//    private static String DBPASS = PropertiesUtils.get("database.properties", "password");
//
//    private static Connection connection;
//    private static DatabaseMetaData dbmd;
//
//    /**
//     * 初始化数据
//     */
//    static {
//        try {
//            // 获取数据库的连接
//            Class.forName(DBDRIVER);
//            connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
//            // 获取meteData
//            dbmd = connection.getMetaData();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    // 获取所有表名
//    private static List<Table> getTable() {
//        List<Table> list = new ArrayList<>();
//        try {
//            // 获取所有表名的结果集
//            ResultSet rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});
//            // 遍历结果集
//            while (rs.next()) {
//                String tableName = rs.getString("TABLE_NAME");
//                Table table = new Table();
//                table.setTableName(tableName);
//                // 去掉下划线，且首字母大写，即对应的java类名
//                table.setClassName(captureName(putOffUnderline(tableName)));
////                System.out.println(table);
//                list.add(table);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    /**
//     * 处理数据库表名，去除下划线
//     * @param columnName
//     * @return
//     */
//    private static String putOffUnderline(String columnName) {
//        StringBuffer fieldNameBuffer = null;
//        String tempNameArray[] = columnName.split("_");
//        for (int i = 0; i < tempNameArray.length; i++) {
//            if (i == 0) {
//                fieldNameBuffer = new StringBuffer(tempNameArray[i]);
//            } else {
//                fieldNameBuffer.append(captureName(tempNameArray[i]));
//            }
//        }
//        return fieldNameBuffer.toString();
//    }
//
//    /**
//     * 返回字符串首字母大写
//     * @param name
//     * @return
//     */
//    private static String captureName(String name) {
//        name = name.substring(0, 1).toUpperCase() + name.substring(1);
//        return name;
//    }
//
//    /**
//     * 根据字段类型返回Java数据类型
//     * @param columnsType
//     * @return
//     */
//    private static String switchType(String columnsType) {
//        switch (columnsType) {
//            case "VARCHAR":
//                return "String";
//            case "BIGINT":
//                return "Long";
//            case "INT":
//                return "Integer";
//            case "DATATIME":
//                return "Date";
//            default:
//                return "String";
//        }
//    }
//
//    /**
//     * 获取表的所有字段
//     * @param tableName
//     * @return
//     */
//    private static List<Columns> getColumns(String tableName) {
//        List<Columns> list = new ArrayList<>();
//        try {
//            ResultSet rs = dbmd.getColumns(null, "%", tableName, "%");
//            while (rs.next()) {
//                Columns columns = new Columns();
//                String columnName = rs.getString("COLUMN_NAME");
//                String columnType = rs.getString("TYPE_NAME");
//                String remarks = rs.getString("REMARKS");
//                columns.setColumnsName(columnName);
//                columns.setColumnType(columnType);
//                columns.setJavaType(switchType(columnType));
//                columns.setFileName(putOffUnderline(columnName));
//                columns.setRemarks(remarks);
//                columns.setUpperFiledName(captureName(columns.getFileName()));
//                list.add(columns);
////                System.out.println(columns);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    /**
//     * 获取所有表信息（包括其所有的字段信息）
//     * @return
//     */
//    public static List<Table> queryDataTables(){
//        List<Table> list = getTable();
//        for (Table table : list) {
//            List<Columns> columns = getColumns(table.getTableName());
//            table.setColumns(columns);
//        }
//        return list;
//    }
//
//}
