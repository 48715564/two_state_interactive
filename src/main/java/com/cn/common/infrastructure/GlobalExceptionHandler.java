package com.cn.common.infrastructure;

import com.cn.common.exception.ResponseException;
import com.cn.page.AjaxResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public AjaxResponse<String> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
//        AjaxResponse<String> ajaxResponse = new AjaxResponse<>();
//        ajaxResponse.setErrorCode("500");
//        ajaxResponse.setMsg(e.getMessage());
//        ajaxResponse.setSuccess(false);
//        ajaxResponse.setResult(null);
//        return ajaxResponse;
//    }

    @ExceptionHandler(value = ResponseException.class)
    @ResponseBody
    public AjaxResponse<String> jsonErrorHandler(HttpServletRequest req, ResponseException e) throws Exception {
        AjaxResponse<String> ajaxResponse = new AjaxResponse<>();
        ajaxResponse.setErrorCode(String.valueOf(e.getStatus()));
        ajaxResponse.setMsg(e.getMessage());
        ajaxResponse.setSuccess(false);
        ajaxResponse.setResult(null);
        return ajaxResponse;
    }
}