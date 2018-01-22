package com.cn.common.service;

import com.github.pagehelper.PageInfo;
import com.cn.page.AjaxResponse;

import java.util.List;

/**
 * Created by bozhou on 2017/11/20.
 */
public class BaseService<T> {
    public AjaxResponse<T> getAjaxResponse(T t){
        AjaxResponse<T> ajaxResponse = new AjaxResponse();
        ajaxResponse.setResult(t);
        return ajaxResponse;
    }

    public AjaxResponse<List<T>> getAjaxResponseList(List<T> list){
        AjaxResponse<List<T>> ajaxResponse = new AjaxResponse();
        ajaxResponse.setResult(list);
        return ajaxResponse;
    }

    public AjaxResponse<PageInfo<T>> getAjaxResponsePage(List<T> list){
        AjaxResponse<PageInfo<T>> ajaxResponse = new AjaxResponse();
        PageInfo<T> pageInfo = new PageInfo(list);
        ajaxResponse.setResult(pageInfo);
        return ajaxResponse;
    }
}
