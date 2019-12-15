package cn.itrip.auth.controller;

import cn.itrip.auth.exception.RedisException;
import cn.itrip.auth.exception.TokenGenerateException;
import cn.itrip.auth.exception.TokenTimeProtecteException;
import cn.itrip.auth.exception.TokenValidateException;
import cn.itrip.auth.service.TokenService;
import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.ItripTokenVO;
import cn.itrip.beans.vo.ItripWechatTokenVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.ErrorCode;
import cn.itrip.common.UrlUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * @description:
 * @author: zeng
 * @createDate: 2019-12-14
 * @version: v1.0
 */
@Controller
@RequestMapping("/vendors")
public class VendorsController {
    @Resource
    private UserService userService;
    @Resource
    private TokenService tokenService;

    @RequestMapping("/wechat/login")
    public void wechatLogin(HttpServletResponse response) throws Exception {
        String redirect_uri = "http://localhost:8080/vendors/wechat/callback";
        // java.net.URLEncoder.encode(redirect_uri, "UTF-8");
        String url = "https://open.weixin.qq.com/connect/qrconnect" +
              "?appid=wx9168f76f000a0d4c" +
              "&redirect_uri=" + java.net.URLEncoder.encode(redirect_uri, "UTF-8") +
              "&response_type=code&scope=snsapi_login" +
              "&state=STATE#wechat_redirect";
        response.sendRedirect(url);
    }

    @RequestMapping(value="/wechat/callback", method = RequestMethod.GET)
    //public void wechatCallback(@RequestParam String code, @RequestParam String state) throws Exception {
    @ResponseBody
    public Dto wechatCallback(@RequestParam String code, @RequestParam String state,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获得code，可以校验是否为null
        if (code == null) {
            return DtoUtil.returnFail("用户未授权", ErrorCode.AUTH_AUTHENTICATION_FAILED);
        }
        // 联合appid、appsecret、code换取access_token
        // 得到JSON
        String accessToken = UrlUtils.loadURL("https://api.weixin.qq.com/sns/oauth2/access_token" +
              "?appid=wx9168f76f000a0d4c" +
              "&secret=8ba69d5639242c3bd3a69dffe84336c1" +
              "&code=" + code +
              "&grant_type=authorization_code");
        Map<String, Object> mapToken = JSON.parseObject(accessToken, Map.class);
        // 用户注册的问题，是否是首次微信账户登录，如果是，insert用户信息
        if (mapToken.get("errcode") != null)
            return DtoUtil.returnFail("获取用户授权失败", ErrorCode.AUTH_AUTHENTICATION_FAILED);
        System.out.println("========================="+mapToken.get("openid")+"===========================");
        ItripUser user = userService.getUserByUserCode(mapToken.get("openid").toString());
        if (user == null) {
            user = new ItripUser();
            user.setActivated(1);
            user.setUserType(1);
            user.setCreationDate(new Date());
            user.setUserCode(mapToken.get("openid").toString());
            user.setUserName(mapToken.get("openid").toString());
            // useGeneratedKeys="true" keyProperty="id"
            userService.itripxAddUserByPhone(user);
            System.out.println("========================="+user.getId()+"===========================");
            // user = userService.getUserByUserCode(mapToken.get("openid").toString());
        }
        // 本站token设置
        ItripTokenVO tokenVO = tokenService.generateToken2(request.getHeader("user-agent"), user);
        tokenService.saveToken(tokenVO.getToken(), user);
        // 页面展示：从微信的二维码页面重定向过来，页面要完全重新生成，跳转应该指向某个页面，随响应携带微信的token信息
        // response.sendRedirect -> xxx.html?access_token=xxxx&refresh_token=xxxx&token=xxxx
        // 临时方案：
        ItripWechatTokenVO wechatTokenVO = new ItripWechatTokenVO(
              tokenVO.getToken(), tokenVO.getExpTime(), tokenVO.getGenTime());
        wechatTokenVO.setAccessToken(mapToken.get("access_token").toString());
        wechatTokenVO.setExpiresIn(mapToken.get("expires_in").toString());
        wechatTokenVO.setOpenid(mapToken.get("openid").toString());
        wechatTokenVO.setRefreshToken(mapToken.get("refresh_token").toString());
        return DtoUtil.returnDataSuccess(wechatTokenVO);
    }

    @RequestMapping(value = "/wechat/user/info", method = RequestMethod.GET)
    @ResponseBody
    public Dto getWechatUserinfo(@RequestParam String accessToken, @RequestParam String openid) throws Exception {
        try {
            String url = "https://api.weixin.qq.com/sns/userinfo" +
                  "?access_token=" + accessToken +
                  "&openid=" + openid;
            String userinfoJSON = UrlUtils.loadURL(url);
            Map<String, Object> userinfo = JSON.parseObject(userinfoJSON, Map.class);
            if (userinfo.get("errcode") != null) {
                return DtoUtil.returnFail("微信用户信息抓取失败", ErrorCode.AUTH_UNKNOWN);
            }
            ItripUser user = userService.getUserByUserCode(userinfo.get("openid").toString());
            if ( ! userinfo.get("nickname").toString().equals(user.getUserName()) ) {
                user.setUserName(userinfo.get("nickname").toString());
                // user.setFlatID(user.getId());
                userService.itripxUpdateUserByPhone(user);
            }
            return DtoUtil.returnDataSuccess(userinfo);
        } catch (Exception e) {
            return DtoUtil.returnFail("加载微信用户信息失败", ErrorCode.AUTH_UNKNOWN);
        }

    }

    @RequestMapping(value = "/wechat/token/refresh", method = RequestMethod.POST)
    @ResponseBody
    public Dto refreshWechatToken(HttpServletRequest request) throws Exception {
        // 网站token置换，之前实现的相同
        // request.getHeader token user-agent
        String token = request.getHeader("token");
        String agent = request.getHeader("user-agent");
        // 业务方法完成置换
        ItripTokenVO tokenVO = null;
        try {
            tokenVO = tokenService.refreshToken(token, agent);
        } catch (TokenValidateException e) {
            return DtoUtil.returnFail("token验证失败", ErrorCode.AUTH_TOKEN_INVALID);
        } catch (TokenTimeProtecteException e) {
            return DtoUtil.returnFail("token置换时间保护", ErrorCode.AUTH_REPLACEMENT_TIME_PROTECTE);
        } catch (TokenGenerateException e) {
            return DtoUtil.returnFail("token生成失败", ErrorCode.AUTH_UNKNOWN);
        } catch (ParseException e) {
            return DtoUtil.returnFail("token验证失败", ErrorCode.AUTH_TOKEN_INVALID);
        } catch (RedisException e) {
            return DtoUtil.returnFail("token保存失败", ErrorCode.AUTH_UNKNOWN);
        }
        // 微信token置换，新实现
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token" +
              "?appid=wx9168f76f000a0d4c" +
              "&grant_type=refresh_token" +
              "&refresh_token=" + request.getHeader("refreshtoken");
        String tokenJSON = UrlUtils.loadURL(url);
        Map<String, Object> wechatToken = JSON.parseObject(tokenJSON, Map.class);
        if (wechatToken.get("errcode") != null) {
            return DtoUtil.returnFail("微信token置换失败", ErrorCode.AUTH_REPLACEMENT_FAILED);
        }
        // 返回综合token信息，和微信认证环节相似
        ItripWechatTokenVO wechatTokenVO = new ItripWechatTokenVO(
              tokenVO.getToken(), tokenVO.getExpTime(), tokenVO.getGenTime());
        wechatTokenVO.setAccessToken(wechatToken.get("access_token").toString());
        wechatTokenVO.setExpiresIn(wechatToken.get("expires_in").toString());
        wechatTokenVO.setOpenid(wechatToken.get("openid").toString());
        wechatTokenVO.setRefreshToken(wechatToken.get("refresh_token").toString());
        return DtoUtil.returnDataSuccess(wechatTokenVO);
    }
}
