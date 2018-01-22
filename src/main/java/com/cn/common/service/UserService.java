package com.cn.common.service;

import com.cn.domain.entity.SysRole;
import com.cn.domain.entity.SysUser;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by bozhou on 2018/1/22.
 */
@Component
public class UserService {

    @Value("${admin.username}")
    private String username;
    @Value("${admin.password}")
    private String password;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public SysUser getUserByUserName(String username) {
        SysUser sysUser = new SysUser();
        sysUser.setId("1");
        sysUser.setUserstatus("01");
        sysUser.setUsername(username);;
        sysUser.setUserpassword(passwordEncoder.encode(password));
        SysRole sysRole = new SysRole();
        sysRole.setId("1");
        sysRole.setName("ADMIN");
        List<SysRole> sysRoleList = Lists.newArrayList();
        sysRoleList.add(sysRole);
        sysUser.setRoleList(sysRoleList);
        return sysUser;
    }
}
