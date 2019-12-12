package cn.itrip.auth.controller;

import cn.itrip.auth.exception.DatabaseException;
import cn.itrip.auth.exception.LoginException;
import cn.itrip.auth.service.TokenService;
import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.ItripTokenVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.ErrorCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: zeng
 * @createDate: 2019-12-11
 * @version: v1.0
 */
@Controller
@RequestMapping("/api")
public class LoginController {
    @Resource
    private UserService userService;
    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/dologin", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto doLogin(@RequestParam String name, @RequestParam String password, HttpServletRequest request) throws Exception {
        ItripUser user = null;
        try {
            if ((user = userService.checkLogin(name, password)) == null) {
                // 用户名密码错误
                return DtoUtil.returnFail("登录失败", ErrorCode.AUTH_AUTHENTICATION_FAILED);
            } else {
                // 登录成功
                System.out.println("login: uesr-agent: "+request.getHeader("user-agent"));
                // 生成TOken
                String token = tokenService.generateToken(request.getHeader("user-agent"), user);
                // 保存Token到redis : key- value key:tokenString value:JSON User
                // PC- 2H, 移动端 无限制
                if (!tokenService.saveToken(token, user)) {
                    return DtoUtil.returnFail("Redis异常", ErrorCode.AUTH_UNKNOWN);
                }
                // 返回Token到客户端, token.split("-") new SimpleDateFormat("").parse(..)
                Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(token.split("-")[3]);
                ItripTokenVO tokenVO = new ItripTokenVO(token,
                      date.getTime() + TokenService.TOKEN_TIMEOUT * 1000, date.getTime());
                return DtoUtil.returnDataSuccess(tokenVO);
            }
        } catch (LoginException e) {
            // 账户未激活
            return DtoUtil.returnFail("账户未激活", ErrorCode.AUTH_NOT_ACTIVATED);
        } catch (DatabaseException e) {
            // 数据库系统错误
            return DtoUtil.returnFail("数据库异常", ErrorCode.AUTH_UNKNOWN);
        }

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto logout(HttpServletRequest request) throws Exception {
        System.out.println("logout: uesr-agent: "+request.getHeader("user-agent"));
        String token = request.getHeader("token");
        String agent = request.getHeader("user-agent");
        if(tokenService.validateToken(token, agent)) {
            // 执行退出
            tokenService.deleteToken(token);
            // 如果有其他业务数据，也需要销毁，未完待续
            // .......
            return DtoUtil.returnSuccess();
        } else {
            // 错误状态，验证失败
            return DtoUtil.returnFail("Token无效", ErrorCode.AUTH_TOKEN_INVALID);
        }
    }
}
