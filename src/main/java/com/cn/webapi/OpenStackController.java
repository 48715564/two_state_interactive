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

import java.util.List;

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

    @ApiOperation(value = "获取openstack相关信息", notes = "获取openstack相关信息,提供翻页查询，按照时间倒叙", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/infoPage")
    public AjaxResponse<List<BusOpenstackLogs>> infoPage(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token){
        try {
            return openStackService.getInfoByPage();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "根据主机ID获取host相关信息", notes = "根据主机ID获取host相关信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getHostById")
    public AjaxResponse getHostById(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,
                                                             @ApiParam(value = "hostID", required = true) @RequestParam("hostID") String hostID){
        try {
            return openStackService.getHostByID(hostID);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "根据虚拟机ID获取虚拟机相关信息", notes = "根据虚拟机ID获取虚拟机相关信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getVMById")
    public AjaxResponse getVMById(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,
                                    @ApiParam(value = "VMID", required = true) @RequestParam("VMID") String VMID){
        try {
            return openStackService.getVMByID(VMID);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "根据网络ID获取网络的相关信息", notes = "根据网络ID获取网络相关信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getNetworkById")
    public AjaxResponse getNetworkById(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,
                                  @ApiParam(value = "networkID", required = true) @RequestParam("networkID") String networkID){
        try {
            return openStackService.getNetworkByID(networkID);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "根据镜像ID获取镜像的相关信息", notes = "根据镜像ID获取镜像相关信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getImagesByID")
    public AjaxResponse getImagesByID(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,
                                           @ApiParam(value = "imagesID", required = true) @RequestParam("imagesID") String imagesID){
        try {
            return openStackService.getImagesByID(imagesID);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "根据实例类型ID获取实例类型的相关信息", notes = "根据实例类型ID获取实例类型相关信息", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @GetMapping("/getFlavorsByID")
    public AjaxResponse getFlavorsByID(@ApiParam(value = "用户token", required = true) @RequestHeader("token") String token,
                                      @ApiParam(value = "flavorsID", required = true) @RequestParam("flavorsID") String flavorsID){
        try {
            return openStackService.getFlavorsByID(flavorsID);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new ResponseException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
