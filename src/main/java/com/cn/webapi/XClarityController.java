package com.cn.webapi;

import com.cn.common.exception.ResponseException;
import com.cn.service.XClarityService;
import com.cn.page.AjaxResponse;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统登陆注册
 * Created by zhoubo on 2016/12/1.
 */
@Api(value = "XClarity", description = "XClarity首页相关api", position = 1)
@RestController
@RequestMapping("/XClarity")
public class XClarityController {
    @Autowired
    private XClarityService xClarityService;
    private static final Logger logger = LoggerFactory.getLogger(XClarityController.class);

    @ApiOperation(value = "获取硬件信息", notes = "获取硬件信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/hardwareList")
    public AjaxResponse<JSONArray> hardwareList(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return xClarityService.hardwareList();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "configPatterns信息", notes = "configPatterns信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/configPatterns")
    public AjaxResponse<JSONObject> configPatterns(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return xClarityService.configPatterns();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "systemImages信息", notes = "systemImages信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/systemImages")
    public AjaxResponse<JSONObject> systemImages(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return xClarityService.systemImages();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "firmwares信息", notes = "firmwares信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/firmwares")
    public AjaxResponse<JSONObject> firmwares(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return xClarityService.firmwares();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "jobs信息", notes = "jobs信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/jobs")
    public AjaxResponse<JSONObject> jobs(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return xClarityService.jobs();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "sessions信息", notes = "sessions信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/sessions")
    public AjaxResponse<JSONArray> sessions(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return xClarityService.sessions();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     *  if (data[i].Utilization <= data[i].Maximum / 4) {
         labelCPU = nls.veryLow;
         color = '#17af4b';
         } else if (data[i].Utilization <= data[i].Maximum / 2) {
         labelCPU = nls.low;
         color = '#75a536';
         } else if (data[i].Utilization <= data[i].Maximum) {
         labelCPU = nls.medium;
         color = '#838329';
         } else if (data[i].Utilization <= data[i].Maximum * 2) {
         labelCPU = nls.high;
         color = '#dd731c';
         } else {
         labelCPU = nls.veryHigh;
         color = '#f04e37';
         }
         data[i].Maximum += " " + nls.cores;
         data[i].Utilization = "<span style='color:" + color + "'>" + labelCPU + "</span>";
     }
     *
     *
     *
     * @return
     */
    @ApiOperation(value = "sysResources信息", notes = "sysResources信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/sysResources")
    public AjaxResponse<JSONArray> sysResources(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return xClarityService.sysResources();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
