package com.cn.page;

import java.util.LinkedHashMap;

/**
 * Created by bozhou on 2017/11/20.
 */
public class AjaxResponse<T> {
    private boolean success = true;// 是否成功
    private String errorCode = "-1";//错误代码
    private String msg = "操作成功";// 提示信息
    private T result;//返回结果

    public AjaxResponse() {
    }

    public AjaxResponse(T result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
