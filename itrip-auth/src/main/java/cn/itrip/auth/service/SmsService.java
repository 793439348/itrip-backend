package cn.itrip.auth.service;

public interface SmsService {
    public void sendMessage(String to, String code) throws Exception;

}
