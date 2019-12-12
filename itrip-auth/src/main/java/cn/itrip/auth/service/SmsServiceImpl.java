package cn.itrip.auth.service;

import cn.itrip.auth.exception.SmsSendException;
import cn.itrip.common.SystemConfig;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author: zeng
 * @createDate: 2019-12-11
 * @version: v1.0
 */
@Service("smsService")
public class SmsServiceImpl implements SmsService{

    @Resource
    private SystemConfig systemConfig;


    @Override
    public void sendMessage(String to, String code) throws Exception {
        Map<String,Object> result = null;

        // 初始化SDK
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

        /**
         * 初始化服务器地址和端口
         * 沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com","8883");
         * 生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com","8883");
         */
        restAPI.init(systemConfig.getSmsServerIP(),systemConfig.getSmsServerPort());

        /**
         * 初始化主账号和主账号令牌，对应官网开发者主账号下的ACCOUNT SID 和 AUTH TOKEN
         * ACOUNT SID 和 AUTH TOKEN在登入官网后，在 ’应用-管理控制台‘ 中查看开发者主账号获取
         * 参数顺序：第一个参数是 ACOUNT SID ,第二个参数是AUTH TOKEN
         */
        restAPI.setAccount(systemConfig.getSmsAccountSid(),systemConfig.getSmsAuthToken());

        /**
         * 初始化应用ID
         * 测试开发可使用’测试Demo' 的APP ID，正式上线需要使用自己创建的应用的APP ID
         * 应用ID 的获取：登入官网，在“应用-应用列表”，点击应用名称，看应用详情获取 APP ID
         */
        restAPI.setAppId(systemConfig.getSmsAppID());
        /**
         * 调用发送模板短信的接口发送短信
         * 参数顺序说明
         * 第一个参数：是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号
         * 第二个参数：是模板ID，在平台上创建的短信模板的ID值；测试时，可以使用系统默认模板，id为1
         * 第三个参数是要替换的内容数组
         */
        result = restAPI.sendTemplateSMS(to, "1", new String[]{code, "3"});

        System.out.println("SDKTestGetSubAccounts result=" + result);
        if ("000000".equals(result.get("statusCode"))) {
            // 正常返回输入data包体信息（map)
            Map<String, Object> data = (Map<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object obj = data.get(key);
                System.out.println(key +" = "+obj);
            }
        }else {
            // 异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
            throw new SmsSendException("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
    }
}
