package cn.itrip.common;

import cn.itrip.beans.pojo.ItripUser;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;


/**
 * @description: Token验证
 * @author: zeng
 * @createDate: 2019-12-10
 * @version: v1.0
 */
public class ValidationToken {
    private Logger logger = Logger.getLogger(this.getClass());
    private RedisAPI redisAPI;

    public RedisAPI getRedisAPI() {
        return redisAPI;
    }

    public void setRedisAPI(RedisAPI redisAPI) {
        this.redisAPI = redisAPI;
    }

    public ItripUser getCurrentUser(String tokenString){
        //根据token从redis中获取用户信息
			/*
			 test token:
			 key : token:1qaz2wsx
			 value : {"id":"100078","userCode":"myusercode","userPassword":"78ujsdlkfjoiiewe98r3ejrf","userType":"1","flatID":"10008989"}
			*/
        ItripUser itripUser = null;
        if(null == tokenString || "".equals(tokenString)){
            return null;
        }
        try{
            String userInfoJson = redisAPI.get(tokenString);
            itripUser = JSONObject.parseObject(userInfoJson,ItripUser.class);
        }catch(Exception e){
            itripUser = null;
            logger.error("get userinfo from redis but is error : " + e.getMessage());
        }
        return itripUser;
    }

    public boolean validateToken(String token, String agent) throws Exception {
        // token确实存在
        if (!redisAPI.exist(token))
            return false;
        // 确认最后6位特征码是否一致
        String code = token.split("-")[4];
        return code.equals(MD5.getMd5(agent, 6));
    }
}
