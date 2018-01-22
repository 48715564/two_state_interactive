package com.cn.common.utils;

import com.cn.common.exception.ResponseException;
import com.xiaoleilu.hutool.cache.Cache;
import com.xiaoleilu.hutool.cache.CacheUtil;
import org.openstack4j.api.OSClient.*;
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
    @Autowired
    private Provider provider;
    private Cache<String,OSClientV3> fifoCache;


    public OSClientV3 getAuthenticateUnscoped(){
        if(fifoCache==null||fifoCache.get("unscoped")==null) {
            OSClientV3 osClientV3 = OSClientV3Factory.authenticateUnscoped(apiHost, userId, password);
            Long timeOut = osClientV3.getToken().getExpires().getTime()-provider.now().getTime();
            fifoCache = CacheUtil.newTimedCache(timeOut-10000);
            fifoCache.put("unscoped",osClientV3);
            return osClientV3;
        }else {
            OSClientV3 osClientV3 = fifoCache.get("unscoped");
            return osClientV3;
        }
    }

    public OSClientV3 getAuthenticateWithProjectScope(String projectId){
        OSClientV3 authenticateUnscoped = getAuthenticateUnscoped();
        if(fifoCache.get(projectId)==null) {
            for (Project project : authenticateUnscoped.identity().projects().list()) {
                if (projectId.equals(project.getId())) {
                    OSClientV3 osClientV3 = OSClientV3Factory.authenticateWithProjectScope(apiHost, authenticateUnscoped.getToken().getUser().getName(), password, project.getDomainId(), project.getId());
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
