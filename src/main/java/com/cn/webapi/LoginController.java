package com.cn.webapi;

import com.fasterxml.jackson.annotation.JsonView;
import com.cn.common.exception.ResponseException;
import com.cn.common.utils.DesUtils;
import com.cn.page.AjaxResponse;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统登陆注册
 * Created by zhoubo on 2016/12/1.
 */
@Api(value = "系统登陆", description = "系统登陆注册相关api", position = 1)
@RestController
@RequestMapping("/sys/login")
public class LoginController {

}
