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


    public OSClientV3 getAuthenticateUnscoped(){
        return OSClientV3Factory.authenticateUnscoped(apiHost, userId, password);
    }

    public OSClientV3 getAuthenticateWithProjectScope(String projectId){
        OSClientV3 authenticateUnscoped = getAuthenticateUnscoped();
            for (Project project : authenticateUnscoped.identity().projects().list()) {
                if (projectId.equals(project.getId())) {
                    OSClientV3 osClientV3 = OSClientV3Factory.authenticateWithProjectScope(apiHost, authenticateUnscoped.getToken().getUser().getName(), password, project.getDomainId(), project.getId());
                    return osClientV3;
                }
        }
        throw new ResponseException("没有找到对应的project", org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
