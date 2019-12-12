package cn.itrip.common;

/**
 * @description: 防js注入公共类
 * @author: zeng
 * @createDate: 2019-12-10
 * @version: v1.0
 */
public class HtmlEncode {
    public static String htmlEncode(String string) {
        if (null == string || "".equals(string)) {
            return null;
        }else {
            String result = string;
            result = result.replaceAll("&", "$amp;");
            result = result.replaceAll("<", "$lt;");
            result = result.replaceAll(">", "$gt;");
            result = result.replaceAll("\"", "&quot;");
            return (result.toString());
        }
    }

    public static String htmlDecode(String string) {
        if (null == string || "".equals(string)) {
            return null;
        }else {
            String result = string;
            result = result.replaceAll("&amp;", "&");
            result = result.replaceAll("&lt;", "<");
            result = result.replaceAll("&gt;", ">");
            result = result.replaceAll("&quot;", "\"");
            return (result.toString());
        }
    }
}
