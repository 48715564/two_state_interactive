package com.cn.service;

import com.cn.common.utils.OpenStackApi;
import com.cn.common.utils.Provider;
import com.cn.domain.entity.BusOpenstackLogs;
import com.cn.domain.entity.BusOpenstackLogsExample;
import com.cn.page.AjaxResponse;
import com.github.pagehelper.PageInfo;
import com.xiaoleilu.hutool.date.DateUtil;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.model.compute.ext.HypervisorStatistics;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.options.PortListOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by bozhou on 2017/12/18.
 */
@Service
public class OpenStackService {
    @Autowired
    OpenStackApi openStackApi;
    @Autowired
    private BusOpenstackLogsService busOpenstackLogsService;
    @Autowired
    private Provider provider;

    public BusOpenstackLogs getCountInfo(){
        BusOpenstackLogs jsonObject = new BusOpenstackLogs();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        HypervisorStatistics hypervisorStatistics =osClientV3.compute().hypervisors().statistics();
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
        List<? extends Network> netList = osClientV3.networking().network().list();
        Integer netWordCount = netList.size();
        jsonObject.setNetworkCount(netWordCount);
        jsonObject.setCreateTm(new Date());
        return jsonObject;
    }

    public AjaxResponse<BusOpenstackLogs> getInfo(){
        BusOpenstackLogs busOpenstackLogs = getCountInfo();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        List imagesList = osClientV3.imagesV2().list();
        busOpenstackLogs.setImagesCount(imagesList.size());
        List flavorsList = osClientV3.compute().flavors().list();
        busOpenstackLogs.setFlavorsCount(flavorsList.size());
        List<? extends Hypervisor> hostList = osClientV3.compute().hypervisors().list();
        List vmList = osClientV3.compute().servers().list();
        //网络个数
        List<? extends Network> netList = osClientV3.networking().network().list();
        Map<String,Object> infoMap = new LinkedHashMap<>();
        infoMap.put("imagesList",imagesList);
        infoMap.put("hostList",hostList);
        infoMap.put("vmList",vmList);
        infoMap.put("netList",netList);
        infoMap.put("flavorsList",flavorsList);
        busOpenstackLogs.setInfoMap(infoMap);
        return new AjaxResponse(busOpenstackLogs);
    }
    public AjaxResponse getHostByID(String hostID) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        Hypervisor hypervisor = osClientV3.compute().hypervisors().list().stream().filter(item->hostID.equals(item.getId())).findFirst().get();
        Map map = new LinkedHashMap();
        map.put("type","host");
        map.put("item",hypervisor);
        ajaxResponse.setResult(map);
        return ajaxResponse;
    }

    public AjaxResponse getNetworkByID(String networkID){
        AjaxResponse ajaxResponse = new AjaxResponse();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        Network network = osClientV3.networking().network().get(networkID);
        List portList = osClientV3.networking().port().list(PortListOptions.create().networkId(networkID));
        Map map = new LinkedHashMap();
        map.put("type","network");
        Map itemMap = new LinkedHashMap();
        itemMap.put("network",network);
        itemMap.put("portList",portList);
        map.put("item",itemMap);
        ajaxResponse.setResult(map);
        return ajaxResponse;
    }

    public AjaxResponse getVMByID(String vmID) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        Server server = osClientV3.compute().servers().get(vmID);
        Map map = new LinkedHashMap();
        map.put("type","vm");
        map.put("item",server);
        ajaxResponse.setResult(map);
        return ajaxResponse;
    }

    public AjaxResponse getImagesByID(String imagesID) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        Image image = osClientV3.imagesV2().get(imagesID);
        Map map = new LinkedHashMap();
        map.put("type","image");
        map.put("item",image);
        ajaxResponse.setResult(map);
        return ajaxResponse;
    }

    public AjaxResponse getFlavorsByID(String flavorsID) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        OSClient.OSClientV3 osClientV3 = openStackApi.getAuthenticateUnscoped();
        Flavor flavor = osClientV3.compute().flavors().get(flavorsID);
        Map<String,String> filterMap = new HashMap<>();
        filterMap.put("flavor",flavorsID);
        List vmList = osClientV3.compute().servers().list(filterMap);
        Map map = new LinkedHashMap();
        Map itemMap = new LinkedHashMap();
        itemMap.put("flavor",flavor);
        itemMap.put("vmList",vmList);
        map.put("type","flavor");
        map.put("item",itemMap);
        ajaxResponse.setResult(map);
        return ajaxResponse;
    }

    public AjaxResponse<List<BusOpenstackLogs>> getInfoByPage(){
        BusOpenstackLogsExample busOpenstackLogsExample = new BusOpenstackLogsExample();
        busOpenstackLogsExample.createCriteria().andCreateTmBetween(DateUtil.offsetDay(provider.now(),-7),provider.now());
        busOpenstackLogsExample.setOrderByClause("create_tm desc");
        AjaxResponse<List<BusOpenstackLogs>> response = busOpenstackLogsService.findList(busOpenstackLogsExample);
        Collections.sort(response.getResult(), Comparator.comparing(BusOpenstackLogs::getCreateTm));
       return response;
    }
}
