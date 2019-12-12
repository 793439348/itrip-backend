package cn.itrip.beans.pojo;//package cn.itrip.beans.pojo;
//
//import cn.itrip.beans.module.Table;
//import cn.itrip.beans.utils.TableHandler;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @description:将数据库记录生成实体类
// * @author: zeng
// * @createDate: 2019-12-11
// * @version: v1.0
// */
//public class MainMaker {
//    private Configuration configuration;
//
//    // 1.完成环境初始化
//    public void init() throws IOException {
//        // 实例化
//        configuration = new Configuration(Configuration.getVersion());
//        // 设置模板所在路径
//        String path = this.getClass().getClassLoader().getResource("").getPath();
//
//        System.out.println(path);
//        configuration.setDirectoryForTemplateLoading(new File(path));
//
//    }
//
//    /**
//     * 获取模板生成相应数据
//     *
//     * @param param
//     * @param tempName 模板文件名
//     * @param fileDir  生成文件保存的位置（记得加上文件名）
//     */
//    public void process(Map<String, Object> param, String tempName, String fileDir) {
//        try {
//            Template template = configuration.getTemplate(tempName);
//            FileOutputStream stream = new FileOutputStream(fileDir);
//            template.process(param, new OutputStreamWriter(stream));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 执行生成实体类
//     * @param args
//     * @throws Exception
//     */
//    public static void main(String[] args) throws Exception {
//        MainMaker genterator = new MainMaker();
//        genterator.init();
//        List<Table> list = TableHandler.queryDataTables();
//        for (Table table : list) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("table", table);
//            genterator.process(map,
//                  "pojo.ftl",
//                  "G:\\itrip-backend\\itrip-beans\\src\\main\\java\\cn\\itrip\\beans\\pojo\\"+
//                        table.getClassName()+".java");
//        }
//    }
//}
