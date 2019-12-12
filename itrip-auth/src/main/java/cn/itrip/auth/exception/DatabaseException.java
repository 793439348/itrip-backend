package cn.itrip.auth.exception;

public class DatabaseException extends Exception {
    public DatabaseException(String msg, Exception e) {
        super(msg, e);
    }
}
