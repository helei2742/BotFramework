package cn.com.helei.common.exception;

public class LoginException extends RuntimeException{

    // 默认构造函数
    public LoginException() {
        super("Account login failed.");
    }

    // 传入错误信息的构造函数
    public LoginException(String message) {
        super(message);
    }

    // 传入错误信息和异常原因的构造函数
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    // 传入异常原因的构造函数
    public LoginException(Throwable cause) {
        super(cause);
    }
}
