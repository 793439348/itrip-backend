package cn.itrip.common;

import cn.itrip.beans.dto.Dto;

/**
 * @description: 用于返回Dto的工具类
 * @author: zeng
 * @createDate: 2019-12-10
 * @version: v1.0
 */
public class DtoUtil {
    public static String success = "true";
    public static String fail = "false";
    public static String errorCode = "0";

    /**
     * 统一返回成功的DTO
     * @return
     */
    public static Dto returnSuccess(){
        Dto dto = new Dto();
        dto.setSuccess(success);
        return dto;
    }

    /**
     * 统一返回成功的DTO  带数据
     * @param message
     * @param data
     * @return
     */
    public static Dto retrunSucess(String message, Object data) {
        Dto dto = new Dto();
        dto.setSuccess(success);
        dto.setMsg(message);
        dto.setErrorCode(errorCode);
        dto.setData(data);
        return dto;
    }

    /**
     * 统一返回成功的DTO 不带数据
     * @param message
     * @return
     */
    public static Dto returnSucess(String message) {
        Dto dto = new Dto();
        dto.setSuccess(success);
        dto.setMsg(message);
        dto.setErrorCode(errorCode);
        return dto;
    }

    /**
     * 统一返回成功的DTO 带数据 没有消息
     * @param data
     * @return
     */
    public static Dto returnDataSuccess(Object data) {
        Dto dto = new Dto();
        dto.setSuccess(success);
        dto.setErrorCode(errorCode);
        dto.setData(data);
        return dto;
    }


    /**
     * 统一返回失败的DTO
     * @param message
     * @param errorCode
     * @return
     */
    public static Dto returnFail(String message, String errorCode) {
        Dto dto = new Dto();
        dto.setSuccess(fail);
        dto.setMsg(message);
        dto.setErrorCode(errorCode);
        return dto;
    }
}

