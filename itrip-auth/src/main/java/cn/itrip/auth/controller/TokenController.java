package cn.itrip.auth.controller;

import cn.itrip.auth.exception.RedisException;
import cn.itrip.auth.exception.TokenGenerateException;
import cn.itrip.auth.exception.TokenTimeProtecteException;
import cn.itrip.auth.exception.TokenValidateException;
import cn.itrip.auth.service.TokenService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.vo.ItripTokenVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.ErrorCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * @description:
 * @author: zeng
 * @createDate: 2019-12-11
 * @version: v1.0
 */
@Controller
@RequestMapping("/api")
public class TokenController {
    @Resource
    private TokenService tokenService;
    @RequestMapping(value = "/retoken", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto refreshToken(HttpServletRequest request) {
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
        // return 新token，同登录
        return DtoUtil.returnDataSuccess(tokenVO);
    }
}
