package cn.itrip.auth.service;

import cn.itrip.auth.exception.DatabaseException;
import cn.itrip.auth.exception.LoginException;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.ErrorCode;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import cn.itrip.dao.user.ItripUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private ItripUserMapper itripUserMapper;
    @Resource
    private RedisAPI redisAPI;

    @Override
    public boolean checkUserByUserCode(String userCode) throws Exception {
        Map params = new HashMap();
        params.put("userCode", userCode);
        Integer result = itripUserMapper.getItripUserCountByMap(params);
        if (result == null || result == 0) {
            return true; // 可用
        } else if (result == 1) {
            return false;   // 不可用
        } else {
            throw new Exception(ErrorCode.AUTH_UNKNOWN);
        }
    }

    public ItripUser getUserByUserCode(String userCode) throws Exception {
        Map params = new HashMap();
        params.put("userCode", userCode);
        List<ItripUser> result = itripUserMapper.getItripUserListByMap(params);
        if (EmptyUtils.isEmpty(result)) {
            return null;
        }else {
            return result.get(0);
        }
    }

    @Override
    public ItripUser checkLogin(String userCode, String password) throws LoginException, DatabaseException {
        Map params = new HashMap();
        params.put("userCode", userCode);
        params.put("userPassword", MD5.getMd5(password, 32));
        List<ItripUser> result = null;
        try {
            result = itripUserMapper.getItripUserListByMap(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("数据库异常", e);
        }
        if (EmptyUtils.isEmpty(result)) {
            return null;
        }else {
            ItripUser user = result.get(0);
            if (user.getActivated()==0) {
                throw new LoginException("账户未激活");
            }
            return user;
        }
    }

    @Override
    public void itripxAddUserByPhone(ItripUser itripUser) throws Exception {
        itripUserMapper.insertItripUser(itripUser);
    }

    @Override
    public void itripxUpdateUserByPhone(ItripUser itripUser) throws Exception {
        itripUserMapper.updateItripUser(itripUser);
    }

    @Override
    public boolean validateUserByUserCode(String phone, String code) throws Exception {
        // 和redis存储的验证码对应一下
        if(redisAPI.exist("authentication:"+phone)) {
            if (redisAPI.get("authentication:"+phone).equals(code)) {
                // 更新数据库用户注册信息
                ItripUser user = this.getUserByUserCode(phone);
                if (user == null)
                    return false;
                user.setUserType(0);
                user.setActivated(1);
                user.setFlatID(user.getId());
                itripUserMapper.updateItripUser(user);
                return true;
            }
        }
        return false;
    }
}
