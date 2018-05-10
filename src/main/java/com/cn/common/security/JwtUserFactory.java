package com.cn.common.security;

import com.cn.domain.entity.SysRole;
import com.cn.domain.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(SysUser sysUser) {
        return new JwtUser(sysUser, mapToGrantedAuthorities(sysUser.getRoleList()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<SysRole> authorities) {
        if(authorities!=null) {
            return authorities.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
