package com.cn.common.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
  @Value("${VM.url}")
  private String url;
  @Value("${VM.username}")
  private String userName;
  @Value("${VM.password}")
  private String password;
  @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
      {
        Constant.url = url;
        Constant.userName = userName;
        Constant.password = password;
      }
}