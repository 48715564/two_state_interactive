package com.cn.webapi;

import com.cn.common.service.XClarityService;
import com.cn.page.AjaxResponse;
import com.xiaoleilu.hutool.json.JSONArray;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统登陆注册
 * Created by zhoubo on 2016/12/1.
 */
@Api(value = "系统登陆", description = "系统登陆注册相关api", position = 1)
@RestController
@RequestMapping("/XClarity")
public class XClarityController {
    @Autowired
    private XClarityService xClarityService;
    @GetMapping("/hardwareList")
    public AjaxResponse<JSONArray> hardwareList(){
        return new AjaxResponse<>(xClarityService.hardwareList());
    }
}
