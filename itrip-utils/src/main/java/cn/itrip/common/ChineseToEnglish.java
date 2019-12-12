package cn.itrip.common;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.logging.Logger;

/**
 * 汉字转拼音的工具类
 */
public class ChineseToEnglish {
    static Logger logger = Logger.getLogger("ChineseToEnglish.class");

    /**
     * 获取首位汉字拼音
     * @param word
     * @return
     */
    public static String getPinYin(String word) {
        char c = word.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
        logger.info("=====word:" + word + " pinyin：" + pinyinArray[0].substring(0, pinyinArray[0].length() - 1) + "=====");
        return pinyinArray[0].substring(0,pinyinArray[0].length()-1);
    }

    public static String getPinYinAll(String word){
        char[] chars = word.toCharArray();
        logger.info("==================="+chars);
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chars[i]);
            sbf.append(pinyinArray[0].substring(0,pinyinArray[0].length()-1));
        }
        logger.info("================"+sbf);
        return sbf.toString();
    }

    public static void main(String[] args) {
        System.out.println(getPinYinAll("你好"));
    }
}
