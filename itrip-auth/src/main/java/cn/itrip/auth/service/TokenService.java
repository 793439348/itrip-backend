package cn.itrip.auth.service;

import cn.itrip.auth.exception.RedisException;
import cn.itrip.auth.exception.TokenGenerateException;
import cn.itrip.auth.exception.TokenTimeProtecteException;
import cn.itrip.auth.exception.TokenValidateException;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.ItripTokenVO;

import java.text.ParseException;

public interface TokenService {
    int TOKEN_TIMEOUT = 2*60*60;//2小时
    //int TOKEN_REPLACEMENT_PROTECTED_TIME = 3600;//1小时
    int TOKEN_REPLACEMENT_PROTECTED_TIME = 30;//30秒
    int TOKEN_DELAY_TIME = 120;//2分钟
    public String generateToken(String agent, ItripUser user) throws Exception;
    public ItripTokenVO generateToken2(String agent, ItripUser user) throws Exception;
    public boolean saveToken(String token, ItripUser user) throws Exception;
    public boolean validateToken(String token, String agent) throws Exception;
    public void deleteToken(String token) throws Exception;
    public ItripTokenVO refreshToken(String token, String agent) throws TokenValidateException, TokenTimeProtecteException,
          TokenGenerateException, ParseException, RedisException;
}
