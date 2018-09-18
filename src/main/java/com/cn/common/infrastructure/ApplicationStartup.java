package com.cn.common.infrastructure;

import com.cn.common.utils.VMClinetUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

  @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
      {
      }
}