package com.cn.common.service;

import com.cn.common.utils.OSClientV3Factory;
import org.openstack4j.api.OSClient.*;
import org.openstack4j.model.telemetry.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by bozhou on 2017/12/18.
 */
@Service
public class IndexService {
    @Autowired
    OSClientV3Factory osClientV3Factory;

    public void test(){
       OSClientV3 os = osClientV3Factory.authenticateUnscoped("","","");
        os.compute().floatingIps().list().forEach(item->{});
        Map<String, ? extends Number> diagnostics = os.compute().servers().diagnostics("serverId");
        os.blockStorage().volumes();
        os.blockStorage().services().list().forEach(item->{

        });
        List<? extends Statistics> stats = os.telemetry().meters().statistics("cpu_util", 320);
        stats.forEach(item->{

        });
//        os.compute().hypervisors().statistics().
    }
}
