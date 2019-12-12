package cn.itrip.auth.service;

import cn.itrip.auth.exception.RedisException;
import cn.itrip.auth.exception.TokenGenerateException;
import cn.itrip.auth.exception.TokenTimeProtecteException;
import cn.itrip.auth.exception.TokenValidateException;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.ItripTokenVO;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import cn.itrip.common.UserAgentUtil;
import cn.itrip.common.ValidationToken;
import com.alibaba.fastjson.JSON;
import cz.mallat.uasparser.UserAgentInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Resource
    private RedisAPI redisAPI;
    @Resource
    private ValidationToken validationToken;

    /*
    token:PC-3066014fa0b10792e4a762-23-20170531133947-4f6496
    前缀：token:
    终端类型：PC / MOBILE
    userCode信息（MD5混淆）：32位
    userID：来自数据库
    Token创建时间：yyyyMMddHHmmss
    最后6位根据User-Agent MD5混淆：6位验证码
    */
    @Override
    public String generateToken(String agent, ItripUser user) throws Exception {
        UserAgentInfo info = UserAgentUtil.getUasParser().parse(agent);
        String deviceType = info.getDeviceType();
        StringBuilder builder = new StringBuilder("token:");
        // Personal computer   --->   PC
        if (deviceType.equals("Personal computer")) {
            builder.append("PC-");
        } else if (deviceType.equals(UserAgentInfo.UNKNOWN)) {// MOBILE or Anknown
            if (UserAgentUtil.CheckAgent(agent)) { // MOBILE
                builder.append("MOBILE-");
            } else {
                builder.append("PC-");
            }
        } else { // MOBILE
            builder.append("MOBILE-");
        }
        builder.append(MD5.getMd5(user.getUserCode(), 32));
        builder.append("-");
        builder.append(user.getId());
        builder.append("-");
        builder.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        builder.append("-");
        builder.append(MD5.getMd5(agent, 6));
        return builder.toString();
    }

    public ItripTokenVO generateToken2(String agent, ItripUser user) throws Exception {
        UserAgentInfo info = UserAgentUtil.getUasParser().parse(agent);
        String deviceType = info.getDeviceType();
        StringBuilder builder = new StringBuilder("token:");
        // Personal computer   --->   PC
        if (deviceType.equals("Personal computer")) {
            builder.append("PC-");
        } else if (deviceType.equals(UserAgentInfo.UNKNOWN)) {// MOBILE or Anknown
            if (UserAgentUtil.CheckAgent(agent)) { // MOBILE
                builder.append("MOBILE-");
            } else {
                builder.append("PC-");
            }
        } else { // MOBILE
            builder.append("MOBILE-");
        }
        builder.append(MD5.getMd5(user.getUserCode(), 32));
        builder.append("-");
        builder.append(user.getId());
        builder.append("-");
        Date date = new Date();
        builder.append(new SimpleDateFormat("yyyyMMddHHmmss").format(date));
        builder.append("-");
        builder.append(MD5.getMd5(agent, 6));

        return new ItripTokenVO(builder.toString(),
              date.getTime()+TokenService.TOKEN_TIMEOUT,
              date.getTime());
    }

    @Override
    public boolean saveToken(String token, ItripUser user) throws Exception {
        if (token.startsWith("token:PC-")) {
            return redisAPI.set(token, TOKEN_TIMEOUT , JSON.toJSONString(user));
        }else {
            return redisAPI.set(token, JSON.toJSONString(user));
        }
    }

    @Override
    public boolean validateToken(String token, String agent) throws Exception {
        return validationToken.validateToken(token, agent);
    }

    @Override
    public void deleteToken(String token) throws Exception {
        redisAPI.delete(token);
    }

    @Override
    public ItripTokenVO refreshToken(String token, String agent)
          throws TokenValidateException, TokenTimeProtecteException,
          TokenGenerateException, ParseException, RedisException {
        // 有效性验证
        if (!redisAPI.exist(token))
            throw new TokenValidateException("无效的token");
        // 不能太频繁，是否达到保护期，登录生成token也会有此问题，验证
        Date genDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(token.split("-")[3]);
        if((Calendar.getInstance().getTimeInMillis()-genDate.getTime())
              < (TokenService.TOKEN_REPLACEMENT_PROTECTED_TIME * 1000)) {
            throw new TokenTimeProtecteException("还在保护期，不允许置换");
        }
        String jsonUser = redisAPI.get(token);
        ItripUser user = JSON.parseObject(jsonUser, ItripUser.class);
        // 生成新token
        ItripTokenVO newToken = null;
        try {
            newToken= this.generateToken2(agent, user);
        } catch (Exception e) {
            throw new TokenGenerateException("token创建失败", e);
        }
        // 新旧token数据移交，token:userinfo
        try {
            this.saveToken(newToken.getToken(), user);
        } catch (Exception e) {
            throw new RedisException("token保存失败");
        }
        // 旧token续命2分钟
        redisAPI.set(token, TokenService.TOKEN_DELAY_TIME, jsonUser);
        // 封装vo
        return newToken;
    }
}
