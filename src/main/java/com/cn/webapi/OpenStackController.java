package com.cn.webapi;

import com.cn.common.exception.ResponseException;
import com.cn.domain.entity.BusOpenstackLogs;
import com.cn.service.OpenStackService;
import com.cn.page.AjaxResponse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 系统登陆注册
 * Created by zhoubo on 2016/12/1.
 */
@Api(value = "OpenStack", description = "OpenStack相关api", position = 1)
@RestController
@RequestMapping("/OpenStack")
public class OpenStackController {
    private static final Logger logger = LoggerFactory.getLogger(OpenStackController.class);
    @Autowired
    private OpenStackService openStackService;

    @ApiOperation(value = "获取openstack相关信息", notes = "获取openstack相关信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/info")
    public AjaxResponse<BusOpenstackLogs> info(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return openStackService.getInfo();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "获取openstack相关信息", notes = "获取openstack相关信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/infoPage")
    public AjaxResponse<PageInfo<BusOpenstackLogs>> infoPage(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,
                                                             @ApiParam(value = "页码", required = true) @RequestParam("page") Integer page,
                                                             @ApiParam(value = "每页条数", required = true) @RequestParam("limit") Integer limit){
        try {
            return openStackService.getInfoByPage(page, limit);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
