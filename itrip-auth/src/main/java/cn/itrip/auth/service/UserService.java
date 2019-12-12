package cn.itrip.auth.service;

import cn.itrip.auth.exception.DatabaseException;
import cn.itrip.auth.exception.LoginException;
import cn.itrip.beans.pojo.ItripUser;

public interface UserService {
    public boolean checkUserByUserCode(String userCode) throws Exception;
    public void itripxAddUserByPhone(ItripUser itripUser) throws Exception;
    public void itripxUpdateUserByPhone(ItripUser itripUser) throws Exception;
    public boolean validateUserByUserCode(String phone, String code) throws Exception;
    public ItripUser getUserByUserCode(String userCode) throws Exception;
    public ItripUser checkLogin(String userCode, String password) throws LoginException, DatabaseException;
}
