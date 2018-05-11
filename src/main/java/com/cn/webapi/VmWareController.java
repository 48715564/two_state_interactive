package com.cn.webapi;

import com.cn.common.exception.ResponseException;
import com.cn.page.AjaxResponse;
import com.cn.service.VMWareService;
import com.vmware.vim25.ObjectContent;
import com.xiaoleilu.hutool.json.JSONArray;
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
 * vmware首页API
 * Created by zhoubo on 2016/12/1.
 */
@Api(value = "vmware", description = "vmware首页API", position = 1)
@RestController
@RequestMapping("/VMware")
public class VmWareController {
    private static final Logger logger = LoggerFactory.getLogger(VmWareController.class);
    @Autowired
    VMWareService vmWareService;

    @ApiOperation(value = "所有的数量信息", notes = "所有的数量信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/all")
    public AjaxResponse<Map<String,List<ObjectContent>>> all(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return vmWareService.getIndexCount();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "所有的数量信息", notes = "所有的数量信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getMonitorData")
    public AjaxResponse<Map<String, Object>> getMonitorData(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,@ApiParam(value = "主机名称", required = true) @RequestParam String hostName){
        try {
            return vmWareService.getMonitorData(hostName);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
