package cn.itrip.common;

import java.util.Collection;
import java.util.Map;

/**
 * @description:判断是否为空的 工具类
 * @author: zeng
 * @createDate: 2019-12-10
 * @version: v1.0
 */
public class EmptyUtils {
    /**
     * 判断为空  true: 空  false：非空
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj){
        if(obj == null)
            return true;
        if(obj instanceof CharSequence)
            return ( (CharSequence)obj).length() == 0;
        if(obj instanceof Collection)
            return ( (Collection)obj).isEmpty();
        if(obj instanceof Map)
            return ( (Map)obj).isEmpty();
        if(obj instanceof Object[]){
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            for (int i = 0; i < object.length; i++) {
                if (!isEmpty(object[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 非空判断， true: 空 false；非空
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj){
        return !isEmpty(obj);
    }

    private boolean validPropertyEmpty(Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (EmptyUtils.isEmpty(args[i])) {
                return true;
            }
        }
        return false;
    }
}
