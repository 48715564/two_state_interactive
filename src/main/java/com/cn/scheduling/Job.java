package com.cn.scheduling;

import com.cn.domain.entity.BusOpenstackLogs;
import com.cn.service.BusOpenstackLogsService;
import com.cn.service.OpenStackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Job {
    private static final Logger logger = LoggerFactory.getLogger(Job.class);

    @Autowired
    private BusOpenstackLogsService busOpenstackLogsService;
    @Autowired
    private OpenStackService openStackService;

    //定时查询openstack信息保存到数据库
    @Scheduled(initialDelay = 3000, fixedRate = 300000)
    public void saveOpenStackInfo() {
        try {
            BusOpenstackLogs busOpenstackLogs = openStackService.getInfo().getResult();
            busOpenstackLogsService.save(busOpenstackLogs);
        }catch (Exception e) {
            logger.error("saveOpenStackInfo:"+e.getMessage());
        }
    }
}
