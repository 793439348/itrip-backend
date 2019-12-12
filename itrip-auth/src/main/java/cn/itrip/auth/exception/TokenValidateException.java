package cn.itrip.auth.exception;

public class TokenValidateException extends Exception {
    public TokenValidateException(String msg) {
        super(msg);
    }
}
