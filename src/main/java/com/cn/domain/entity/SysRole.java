package com.cn.domain.entity;

import com.cn.common.persistence.BaseEntity;

public class SysRole extends BaseEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}