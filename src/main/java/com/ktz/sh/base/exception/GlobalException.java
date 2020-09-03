package com.ktz.sh.base.exception;

/**
 * @ClassName GlobalException
 * @Description 自定义全局异常类
 * @Author 开拓者-骚豪
 * @Date 2020/9/3 8:59
 * @Version V1.0.0
 **/
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = -5022432367686363731L;

    private int code;


    public GlobalException(GlobalException e) {
        super(e.getMessage());
        this.code = e.getCode();
    }

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
