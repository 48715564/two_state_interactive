package com.cn.webapi;

import com.cn.common.exception.ResponseException;
import com.cn.page.AjaxResponse;
import com.cn.service.OvirtService;
import com.xiaoleilu.hutool.json.JSONObject;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ovirt首页API
 * Created by zhoubo on 2016/12/1.
 */
@Api(value = "ovirt", description = "ovirt首页API", position = 1)
@RestController
@RequestMapping("/ovirt")
public class OvirtController {
    private static final Logger logger = LoggerFactory.getLogger(OvirtController.class);
    @Autowired
    OvirtService ovirtService;

    @ApiOperation(value = "首页信息", notes = "获取首页信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/indexInfo")
    public AjaxResponse<JSONObject> indexInfo(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return ovirtService.getIndexInfo();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "监控信息", notes = "获取监控信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/monitorData")
    public AjaxResponse<Map<String, Object>> monitorData(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return ovirtService.monitorData();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "监控磁盘IOPS信息", notes = "监控磁盘IOPS信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/monitorStoreBandWidthData")
    public AjaxResponse<Map<String, List<String>>> monitorStoreBandWidthData(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return ovirtService.monitorStoreBandWidthData();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "监控磁盘最新的带宽信息", notes = "监控磁盘最新的带宽信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/monitorNowStoreIOPSData")
    public AjaxResponse<Map<String, String>> monitorNowStoreIOPSData(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return ovirtService.monitorNowStoreIOPSData();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
