package com.cn.common.utils;

import com.cn.common.exception.ResponseException;
import com.xiaoleilu.hutool.cache.Cache;
import com.xiaoleilu.hutool.cache.CacheUtil;
import org.openstack4j.api.OSClient.*;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.identity.v3.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by bozhou on 2018/1/22.
 */
@Component
public class OpenStackApi {
    @Value("${OpenStack.userId}")
    private String userId;
    @Value("${OpenStack.password}")
    private String password;
    @Value("${OpenStack.identity3Url}")
    private String apiHost;
    @Value("${OpenStack.application-catalog-url:}")
    private String applicationCatalog;
    @Value("${OpenStack.compute-url:}")
    private String compute;
    @Value("${OpenStack.image-url:}")
    private String image;
    @Value("${OpenStack.volume-url:}")
    private String volume;
    @Value("${OpenStack.object-store-url:}")
    private String objectStore;
    @Value("${OpenStack.network-url:}")
    private String networkUrl;
    @Value("${OpenStack.load-balancer-url:}")
    private String loadBalancer;
    @Value("${OpenStack.ec2-url:}")
    private String ec2;
    @Value("${OpenStack.metering-url:}")
    private String metering;
    @Value("${OpenStack.alarming-url:}")
    private String alarming;
    @Value("${OpenStack.orchestration-url:}")
    private String orchestration;
    @Value("${OpenStack.clustering-url:}")
    private String clustering;
    @Value("${OpenStack.data_processing-url:}")
    private String dataProcessing;
    @Value("${OpenStack.share-url:}")
    private String share;
    @Value("${OpenStack.database-url:}")
    private String database;
    @Value("${OpenStack.key-manager-url:}")
    private String keyManager;
    @Value("${OpenStack.nfv-orchestration-url:}")
    private String nfvOrchestration;
    @Value("${OpenStack.artifact-url:}")
    private String artifact;
    @Value("${OpenStack.container-url:}")
    private String container;
    @Value("${OpenStack.dns-url:}")
    private String dns;
    @Value("${OpenStack.workflow-url:}")
    private String workflow;
    @Autowired
    private Provider provider;
    private ThreadLocal<Cache<String,OSClientV3>> threadLocal = new ThreadLocal<>();
    private OverridableEndpointURLResolver endpointResolver;

    private ServiceType getServiceByTypeStr(String type){
        ServiceType serviceType = ServiceType.UNKNOWN;
        switch (type){
            case "applicationCatalog":serviceType=ServiceType.APP_CATALOG;break;
            case "compute":serviceType=ServiceType.COMPUTE;break;
            case "image":serviceType=ServiceType.IMAGE;break;
            case "volume":serviceType=ServiceType.BLOCK_STORAGE;break;
            case "objectStore":serviceType=ServiceType.OBJECT_STORAGE;break;
            case "network":serviceType=ServiceType.NETWORK;break;
            case "loadBalancer":serviceType=ServiceType.OCTAVIA;break;
            case "ec2":serviceType=ServiceType.EC2;break;
            case "metering":serviceType=ServiceType.TELEMETRY;break;
            case "alarming":serviceType=ServiceType.TELEMETRY_AODH;break;
            case "orchestration":serviceType=ServiceType.ORCHESTRATION;break;
            case "clustering":serviceType=ServiceType.CLUSTERING;break;
            case "dataProcessing":serviceType=ServiceType.SAHARA;break;
            case "share":serviceType=ServiceType.SHARE;break;
            case "database":serviceType=ServiceType.DATABASE;break;
            case "keyManager":serviceType=ServiceType.BARBICAN;break;
            case "nfvOrchestration":serviceType=ServiceType.TACKER;break;
            case "artifact":serviceType=ServiceType.ARTIFACT;break;
            case "container":serviceType=ServiceType.MAGNUM;break;
            case "dns":serviceType=ServiceType.DNS;break;
            case "workflow":serviceType=ServiceType.WORKFLOW;break;
        }
        return serviceType;
    }


    public OpenStackApi(){
        if(endpointResolver==null){
            endpointResolver = new OverridableEndpointURLResolver();
            if(StringUtils.isNotBlank(applicationCatalog)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("applicationCatalog"),applicationCatalog);
            }
            if(StringUtils.isNotBlank(compute)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("compute"),compute);
            }
            if(StringUtils.isNotBlank(image)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("image"),image);
            }
            if(StringUtils.isNotBlank(volume)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("volume"),volume);
            }
            if(StringUtils.isNotBlank(objectStore)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("objectStore"),objectStore);
            }
            if(StringUtils.isNotBlank(networkUrl)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("networkUrl"),networkUrl);
            }
            if(StringUtils.isNotBlank(loadBalancer)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("loadBalancer"),loadBalancer);
            }
            if(StringUtils.isNotBlank(ec2)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("ec2"),ec2);
            }
            if(StringUtils.isNotBlank(metering)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("metering"),metering);
            }
            if(StringUtils.isNotBlank(alarming)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("alarming"),alarming);
            }
            if(StringUtils.isNotBlank(orchestration)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("orchestration"),orchestration);
            }
            if(StringUtils.isNotBlank(clustering)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("clustering"),clustering);
            }
            if(StringUtils.isNotBlank(dataProcessing)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("dataProcessing"),dataProcessing);
            }
            if(StringUtils.isNotBlank(share)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("share"),share);
            }
            if(StringUtils.isNotBlank(database)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("database"),database);
            }
            if(StringUtils.isNotBlank(keyManager)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("keyManager"),keyManager);
            }
            if(StringUtils.isNotBlank(nfvOrchestration)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("nfvOrchestration"),nfvOrchestration);
            }
            if(StringUtils.isNotBlank(artifact)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("artifact"),artifact);
            }
            if(StringUtils.isNotBlank(container)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("container"),container);
            }
            if(StringUtils.isNotBlank(dns)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("dns"),dns);
            }
            if(StringUtils.isNotBlank(workflow)){
                endpointResolver.addOverrideEndpoint(getServiceByTypeStr("workflow"),workflow);
            }
        }
    }

    public OSClientV3 getAuthenticateUnscoped(){
        if(threadLocal.get()==null||threadLocal.get().get("unscoped")==null) {
            OSClientV3 osClientV3 = null;
            if(endpointResolver!=null&&!endpointResolver.getOverrides().isEmpty()) {
                osClientV3 = OSClientV3Factory.authenticateUnscoped(endpointResolver, apiHost, userId, password);
            }
            else{
                osClientV3=OSClientV3Factory.authenticateUnscoped(apiHost, userId, password);
            }
            Long timeOut = osClientV3.getToken().getExpires().getTime()-provider.now().getTime();
            Cache<String,OSClientV3> fifoCache = CacheUtil.newTimedCache(timeOut-10000);
            fifoCache.put("unscoped",osClientV3);
            threadLocal.set(fifoCache);
            return osClientV3;
        }else {
            OSClientV3 osClientV3 = threadLocal.get().get("unscoped");
            return osClientV3;
        }
    }

    public OSClientV3 getAuthenticateWithProjectScope(String projectId){
        OSClientV3 authenticateUnscoped = getAuthenticateUnscoped();
        Cache<String,OSClientV3> fifoCache = threadLocal.get();
        if(fifoCache.get(projectId)==null) {
            for (Project project : authenticateUnscoped.identity().projects().list()) {
                if (projectId.equals(project.getId())) {
                    OSClientV3 osClientV3 = null;
                    if(endpointResolver!=null&&!endpointResolver.getOverrides().isEmpty()) {
                        osClientV3 =OSClientV3Factory.authenticateWithProjectScope(endpointResolver,apiHost, authenticateUnscoped.getToken().getUser().getName(), password, project.getDomainId(), project.getId());
                    }else{
                        osClientV3 =OSClientV3Factory.authenticateWithProjectScope(apiHost, authenticateUnscoped.getToken().getUser().getName(), password, project.getDomainId(), project.getId());
                    }
                    fifoCache.put(projectId, osClientV3);
                    return osClientV3;
                }
            }
            throw new ResponseException("没有找到对应的project", org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            return fifoCache.get(projectId);
        }
    }
}
