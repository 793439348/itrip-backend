package cn.itrip.common;

import java.math.BigDecimal;

/**
 * BigDecimal的加减乘除
 */
public class BigDecimalUtil {

    /**
     * BigDecimal操作枚举定义
     */
    public enum BigDecimalOprations{
        add,subtract,multiply,divide
    }

    /**
     * BigDecimal加减乘除运算
     * @param numOne
     * @param numTwo
     * @param bigDecimalOprations
     * @param scale
     * @param roundingMode
     * @return
     * @throws Exception
     */
    public static BigDecimal OperationASMD(Object numOne,Object numTwo,BigDecimalOprations bigDecimalOprations,int scale,int roundingMode)throws Exception{
        BigDecimal num1 = new BigDecimal(String.valueOf(numOne)).setScale(scale,roundingMode);
        BigDecimal num2 = new BigDecimal(String.valueOf(numTwo)).setScale(scale,roundingMode);
        switch (bigDecimalOprations){
            case add:return num1.add(num2).setScale(scale,roundingMode);
            case subtract:return num1.subtract(num2).setScale(scale,roundingMode);
            case multiply:return num1.multiply(num2).setScale(scale,roundingMode);
            case divide:return num1.divide(num2).setScale(scale,roundingMode);
        }
        return null;
    }



}
