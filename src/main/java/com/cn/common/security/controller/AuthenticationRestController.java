package com.cn.common.security.controller;

import com.cn.common.exception.ResponseException;
import com.cn.common.security.JwtAuthenticationRequest;
import com.cn.common.security.JwtTokenUtil;
import com.cn.common.security.JwtUser;
import com.cn.common.security.service.JwtAuthenticationResponse;
import com.cn.common.service.UserService;
import com.cn.domain.entity.SysUser;
import com.cn.page.AjaxResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(value = "JWT相关接口", description = "相关接口相关api", position = 1)
@RestController
public class AuthenticationRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登陆", notes = "用户名密码登录登陆。", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public AjaxResponse<?> createAuthenticationToken(JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {
        // Perform the security
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload password post-security so we can generate token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails, device);
            SysUser user = userService.getUserByUserName(authenticationRequest.getUsername());
//            map.put("member", user);
            // Return the token
            return new AjaxResponse<>(new JwtAuthenticationResponse(token));
        }catch (Exception e){
            throw new ResponseException("用户名或者密码错误！",HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "用户token刷新", notes = "用户token刷新。", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.POST)
    public AjaxResponse<?> refreshAndGetAuthenticationToken(@ApiParam(value = "用户token", required = true) @RequestParam(required = true) String token) {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.canTokenBeRefreshed(token, user.getSysUser().getLastPasswordResetDate())) {
                String refreshedToken = jwtTokenUtil.refreshToken(token);
                return new AjaxResponse<>(new JwtAuthenticationResponse(refreshedToken));
            } else {
                throw new ResponseException("登录已过期，请重新登录！", HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            throw new ResponseException("登录已过期，请重新登录！", HttpStatus.UNAUTHORIZED);
        }
    }

    //检测token是否失效
    @ApiOperation(value = "检测token是否失效", notes = "检测token是否失效。", position = 1)
    @ApiResponses({@ApiResponse(code = 200, message = "运行结果")})
    @RequestMapping(value = "isTokenExpired", method = RequestMethod.POST)
    public AjaxResponse<?> isTokenExpired( @ApiParam(value = "用户token", required = true) @RequestParam(required = true) String token) {
        try {
          return new AjaxResponse<>(!jwtTokenUtil.isTokenExpired(token));
        }catch (Exception e){
            return new AjaxResponse<>(false);
        }
    }

}
