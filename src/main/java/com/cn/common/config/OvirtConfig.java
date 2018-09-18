package com.cn.common.config;

import org.ovirt.engine.sdk4.Connection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.ovirt.engine.sdk4.ConnectionBuilder.connection;

@Configuration
public class OvirtConfig {
    @Value("${ovirt.url}")
    private String ovirtUrl;
    @Value("${ovirt.username}")
    private String ovirtUsername;
    @Value("${ovirt.password}")
    private String ovirtPassword;
    @Value("${ovirt.trustStoreFile}")
    private String ovirtTrustStoreFile;

    @Bean
    public Connection getConnection() {
        return connection()
                .url(ovirtUrl)
                .user(ovirtUsername)
                .password(ovirtPassword)
                .trustStoreFile(ovirtTrustStoreFile)
                .build();

    }
}
