package com.ktz.sh.base.exception;

import com.ktz.sh.base.response.ResponseCode;
import com.ktz.sh.base.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 全局异常处理器
 * @Author 开拓者-骚豪
 * @Date 2020/9/3 9:05
 * @Version V1.0.0
 **/
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    @ResponseBody
    public ResponseData jsonErrorHandler(HttpServletRequest request, GlobalException e) throws Exception {
        log.error("", e);
        ResponseData response = new ResponseData();
        response.setMessage(e.getMessage());
        response.setCode(e.getCode());
        return response;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData defaultErrorHandler(HttpServletRequest request, Exception e) {
        log.error("", e);
        ResponseData response = new ResponseData();
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            response.setCode(ResponseCode.NOT_FOUND.getCode());
            response.setMessage(e.getMessage());
        } else {
            response.setCode(ResponseCode.SERVER_ERROR.getCode());
            response.setMessage(ResponseCode.SERVER_ERROR.getMsg());
        }
        return response;
    }
}
