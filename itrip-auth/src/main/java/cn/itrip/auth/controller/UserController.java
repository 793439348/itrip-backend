package cn.itrip.auth.controller;

import cn.itrip.auth.exception.RedisException;
import cn.itrip.auth.service.SmsService;
import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.userinfo.ItripUserVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.ErrorCode;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: zeng
 * @createDate: 2019-12-11
 * @version: v1.0
 */
@Controller
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private SmsService smsService;
    @Resource
    private RedisAPI redisAPI;

    @RequestMapping(value = "/registerbyphone", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto registerByPhone(@RequestBody ItripUserVO userVO) throws RedisException {
        // 手机格式检查
        if (this.validatePhone(userVO.getUserCode())) {
            // 重复注册检查,去数据库中查找，判断用户名是否可用
            try {
                if (userService.checkUserByUserCode(userVO.getUserCode())) {
                    String code = Integer.toString(MD5.getRandomCode());
                    // 发送短信
                    smsService.sendMessage(userVO.getUserCode(), code);
                    //redis缓存验证码信息
                    boolean isOk = redisAPI.set("authentication:" + userVO.getUserCode(), 180, code);
                    if (!isOk) {
                        throw new RedisException("Redis写入异常");
                    }
                    // 保存用户信息
                    ItripUser user = new ItripUser();
                    user.setUserPassword(MD5.getMd5(userVO.getUserPassword(), 32));
                    user.setUserCode(userVO.getUserCode());
                    user.setUserName(userVO.getUserName());
                    user.setCreationDate(new Date());
                    userService.itripxAddUserByPhone(user);

                    return DtoUtil.returnSuccess();
                } else {
                    return DtoUtil.returnFail("手机号码已被注册！", ErrorCode.AUTH_USER_ALREADY_EXISTS);
                }
            } catch (Exception e) {
                // log
                return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_UNKNOWN);
            }
        } else {
            return DtoUtil.returnFail("手机格式错误！", ErrorCode.AUTH_ILLEGAL_USERCODE);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/registerByEmail", method = RequestMethod.POST, produces = "application/json")
    public Dto registerByEmail(@RequestBody ItripUserVO userVO) throws Exception {
        // 验证邮箱格式
        if(validataEmail(userVO.getUserCode())){
            // 验证邮箱是否已被使用
            if(userService.checkUserByUserCode(userVO.getUserCode())){
                // 生成验证码，发送邮箱
                String code = Integer.toString(MD5.getRandomCode());

            }
        }
        return null;
    }

    @RequestMapping(value = "/validatephone", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public Dto validatePhone(@RequestParam String user, @RequestParam String code) throws Exception {
        System.out.println("get : " + user + "\t" + code);
        if (userService.validateUserByUserCode(user, code)) {
            return DtoUtil.returnSuccess();
        } else {
            return DtoUtil.returnFail("认证失败，请重新注册", ErrorCode.AUTH_ACTIVATE_FAILED);
        }
    }

    // 验证手机格式
    protected boolean validatePhone(String phone) {
        String regex = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
        return Pattern.compile(regex).matcher(phone).find();
    }

    // 验证邮箱格式
    protected static boolean validataEmail(String email) {
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

}