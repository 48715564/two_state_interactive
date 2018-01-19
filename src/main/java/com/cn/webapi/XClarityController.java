package com.cn.webapi;

import com.cn.common.exception.ResponseException;
import com.cn.common.service.XClarityService;
import com.cn.page.AjaxResponse;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @ApiOperation(value = "获取硬件信息", notes = "获取硬件信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/hardwareList")
    public AjaxResponse<JSONArray> hardwareList(){
        try {
            return xClarityService.hardwareList();
        }catch (Exception e){
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "configPatterns信息", notes = "configPatterns信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/configPatterns")
    public AjaxResponse<JSONObject> configPatterns(){
        try {
            return xClarityService.configPatterns();
        }catch (Exception e){
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "systemImages信息", notes = "systemImages信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/systemImages")
    public AjaxResponse<JSONObject> systemImages(){
        try {
            return xClarityService.systemImages();
        }catch (Exception e){
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "firmwares信息", notes = "firmwares信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/firmwares")
    public AjaxResponse<JSONObject> firmwares(){
        try {
            return xClarityService.firmwares();
        }catch (Exception e){
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "jobs信息", notes = "jobs信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/jobs")
    public AjaxResponse<JSONObject> jobs(){
        try {
            return xClarityService.jobs();
        }catch (Exception e){
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "sessions信息", notes = "sessions信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/jobs")
    public AjaxResponse<JSONArray> sessions(){
        try {
            return xClarityService.sessions();
        }catch (Exception e){
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "sysResources信息", notes = "sysResources信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/sysResources")
    public AjaxResponse<JSONArray> sysResources(){
        try {
            return xClarityService.sysResources();
        }catch (Exception e){
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
