package com.cn.service;

import com.cn.common.utils.OpenStackApi;
import com.cn.domain.entity.BusOpenstackLogs;
import com.cn.domain.entity.BusOpenstackLogsExample;
import com.cn.page.AjaxResponse;
import com.github.pagehelper.PageInfo;
import org.openstack4j.model.compute.ext.HypervisorStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by bozhou on 2017/12/18.
 */
@Service
public class OpenStackService {
    @Autowired
    OpenStackApi openStackApi;
    @Autowired
    private BusOpenstackLogsService busOpenstackLogsService;


    public AjaxResponse<BusOpenstackLogs> getInfo(){
        BusOpenstackLogs jsonObject = new BusOpenstackLogs();
        HypervisorStatistics hypervisorStatistics =openStackApi.getAuthenticateUnscoped().compute().hypervisors().statistics();
        jsonObject.setHypervisorCount(hypervisorStatistics.getCount());
        jsonObject.setRunningVmCount(hypervisorStatistics.getRunningVM());
        jsonObject.setDiskAvailableLeast(hypervisorStatistics.getLeastAvailableDisk());
        jsonObject.setFreeDisk(hypervisorStatistics.getFreeDisk());
        jsonObject.setLocal(hypervisorStatistics.getLocal());
        jsonObject.setLocalUsed(hypervisorStatistics.getLocalUsed());
        jsonObject.setFreeRam(hypervisorStatistics.getFreeRam());
        jsonObject.setMemory(hypervisorStatistics.getMemory());
        jsonObject.setMemoryUsed(hypervisorStatistics.getMemoryUsed());
        jsonObject.setVirtualCpu(hypervisorStatistics.getVirtualCPU());
        jsonObject.setVirtualUsedCpu(hypervisorStatistics.getVirtualUsedCPU());

        //网络个数
        Integer netWordCount = openStackApi.getAuthenticateUnscoped().networking().network().list().size();
        jsonObject.setNetworkCount(netWordCount);
        jsonObject.setCreateTm(new Date());
        return new AjaxResponse(jsonObject);
    }

    public AjaxResponse<PageInfo<BusOpenstackLogs>> getInfoByPage(Integer page, Integer limit){
        BusOpenstackLogsExample busOpenstackLogsExample = new BusOpenstackLogsExample();
        busOpenstackLogsExample.setOrderByClause("create_tm desc");
       return busOpenstackLogsService.findPage(page,limit,busOpenstackLogsExample);
    }
}
