package com.cn.webapi;

import com.cn.common.exception.ResponseException;
import com.cn.page.AjaxResponse;
import com.cn.service.VMWareService;
import com.vmware.vim25.ObjectContent;
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
 * Created by zhoubo on 2017/12/1.
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

    @ApiOperation(value = "指定主机的监控信息", notes = "指定主机的监控信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getHostMonitorData")
    public AjaxResponse<Map<String, Object>> getHostMonitorData(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,@ApiParam(value = "主机名称", required = true) @RequestParam String hostName){
        try {
            return vmWareService.getHostMonitorData(hostName);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "指定数据中心的监控信息", notes = "指定数据中心的监控信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getDataCenterMonitorData")
    public AjaxResponse<Map<String, Object>> getDataCenterMonitorData(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,@ApiParam(value = "数据中心名称", required = true) @RequestParam String dataCenterName){
        try {
            return vmWareService.getDataCenterMonitorData(dataCenterName);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "指定集群的监控信息", notes = "指定集群的监控信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getClusterMonitorData")
    public AjaxResponse<Map<String, Object>> getClusterMonitorData(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,@ApiParam(value = "集群名称", required = true) @RequestParam String clusterName){
        try {
            return vmWareService.getClusterMonitorData(clusterName);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "指定虚拟机的监控信息", notes = "指定虚拟机的监控信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getVMMonitorData")
    public AjaxResponse<Map<String, Object>> getVMMonitorData(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,@ApiParam(value = "集群名称", required = true) @RequestParam String vmName){
        try {
            return vmWareService.getVMMonitorData(vmName);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "指定存储的监控信息", notes = "指定存储的监控信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getDSMonitorData")
    public AjaxResponse<Map<String, Object>> getDSMonitorData(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,@ApiParam(value = "存储名称", required = true) @RequestParam String dsName){
        try {
            return vmWareService.getDataStoreMonitorData(dsName);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "指定网络的监控信息", notes = "指定网络的监控信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getNetworkMonitorData")
    public AjaxResponse<Map<String, Object>> getNetworkMonitorData(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,@ApiParam(value = "网络名称", required = true) @RequestParam String networkName){
        try {
            return vmWareService.getNetworkMonitorData(networkName);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
