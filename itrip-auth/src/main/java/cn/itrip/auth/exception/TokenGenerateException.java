package cn.itrip.auth.exception;

public class TokenGenerateException  extends  Exception{
    public TokenGenerateException(String msg, Exception e) {
        super(msg, e);
    }
}
