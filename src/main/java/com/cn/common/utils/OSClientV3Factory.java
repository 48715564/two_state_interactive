package com.cn.common.utils;

import org.openstack4j.api.OSClient.*;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.ext.HypervisorStatistics;
import org.openstack4j.model.identity.v3.Endpoint;
import org.openstack4j.openstack.OSFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by bozhou on 2017/12/18.
 */
@Component
public class OSClientV3Factory {

    public static OSClientV3 authenticateUnscoped(OverridableEndpointURLResolver endpointResolver,String endpoint,String user_id, String secret){
        OSClientV3 os = OSFactory.builderV3().withConfig(Config.newConfig().withEndpointURLResolver(endpointResolver))
                .endpoint(endpoint)
                .credentials(user_id, secret)
                .authenticate();
        return os;
    }

    public static OSClientV3 authenticateWithProjectScope(OverridableEndpointURLResolver endpointResolver,String endpoint,String user_id, String secret,String user_domain_id,String project_id){
        OSClientV3 os = OSFactory.builderV3().withConfig(Config.newConfig().withEndpointURLResolver(endpointResolver))
                .endpoint(endpoint)
                .credentials(user_id, secret, Identifier.byId(user_domain_id))
                .scopeToProject(Identifier.byId(project_id))
                .authenticate();
        return os;
    }


    public static OSClientV3 authenticateWithDomainScope(OverridableEndpointURLResolver endpointResolver,String endpoint,String user_id, String secret,String user_domain_id,String domain_id){
        OSClientV3 os = OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(user_id, secret, Identifier.byId(user_domain_id))
                .scopeToDomain(Identifier.byId(domain_id))
                .authenticate();
        return os;
    }

    public static OSClientV3 authenticateWithAToken(OverridableEndpointURLResolver endpointResolver,String endpoint,String token_id, String project_id){
        OSClientV3 os = OSFactory.builderV3().withConfig(Config.newConfig().withEndpointURLResolver(endpointResolver))
                .endpoint(endpoint)
                .token(token_id)
                .scopeToProject(Identifier.byId(project_id))
                .authenticate();
        return os;
    }


    public static OSClientV3 authenticateUnscoped(String endpoint,String user_id, String secret){
        OSClientV3 os = OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(user_id, secret)
                .authenticate();
        return os;
    }

    public static OSClientV3 authenticateWithProjectScope(String endpoint,String user_id, String secret,String user_domain_id,String project_id){
        OSClientV3 os = OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(user_id, secret, Identifier.byId(user_domain_id))
                .scopeToProject(Identifier.byId(project_id))
                .authenticate();
        return os;
    }


    public static OSClientV3 authenticateWithDomainScope(String endpoint,String user_id, String secret,String user_domain_id,String domain_id){
        OSClientV3 os = OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(user_id, secret, Identifier.byId(user_domain_id))
                .scopeToDomain(Identifier.byId(domain_id))
                .authenticate();
        return os;
    }

    public static OSClientV3 authenticateWithAToken(String endpoint,String token_id, String project_id){
        OSClientV3 os = OSFactory.builderV3()
                .endpoint(endpoint)
                .token(token_id)
                .scopeToProject(Identifier.byId(project_id))
                .authenticate();
        return os;
    }

    public static void main(String[] args) {
        // 添加各个服务的endpoint绑定
        OverridableEndpointURLResolver endpointResolver = new OverridableEndpointURLResolver();
        OSClientV2 os3 = OSFactory.builderV2()
                .endpoint("http://10.0.2.5:5000/v2.0")
                .credentials("admin","zNPcGtjQ0T1Pu5xf3iPqIrtGXbRcxCMW9fdUcTyJ")
                .tenantName("admin")
                .authenticate();
//        endpointResolver.addOverrideEndpoint(ServiceType.COMPUTE,
//                "http://192.168.0.148:8774/v2.1/cc46d392edd740f0859bedc75e07bcff");
//
//        endpointResolver.addOverrideEndpoint(ServiceType.IMAGE,
//                "https://as.example.com/autoscaling-api/v1/%(project_id)s");

//        System.out.println(Identifier.byName("default").toString());
        OSClientV3 os1 = OSFactory.builderV3()
                .endpoint("http://10.0.2.5:5000/v3")
                .credentials("478a780e165e438e8971b069b96a88cb", "zNPcGtjQ0T1Pu5xf3iPqIrtGXbRcxCMW9fdUcTyJ")
                .authenticate();
//        OSClientV3 os =  OSFactory.builderV3().endpoint("http://10.0.2.5:5000/v3").credentials("admin", "zNPcGtjQ0T1Pu5xf3iPqIrtGXbRcxCMW9fdUcTyJ", Identifier.byName("default"))
//                .scopeToDomain(Identifier.byId("default"))
//                .authenticate();
        OSClientV3 os = OSFactory.builderV3()
                .endpoint("http://10.0.2.5:5000/v3")
                .credentials("admin", "zNPcGtjQ0T1Pu5xf3iPqIrtGXbRcxCMW9fdUcTyJ", Identifier.byName("default"))
                .scopeToProject(Identifier.byId("0b176b5387944eb3a982648bb821fbae"))
                .authenticate();

//        OSClientV3 os = OSFactory.builderV3().withConfig(Config.newConfig().withEndpointURLResolver(endpointResolver))
//                .endpoint("http://identity.daocloud.cc/v3")
//                .credentials("admin", "FQiYa5tlBDJu4C5kqvQPEu4Z5nvfeSYXA1aMltl1", Identifier.byId("default"))
//                .scopeToProject(Identifier.byId("ca2a6d04c5f94f0e8c696f22b1ac5819"))
//                .authenticate();

//        List<? extends Endpoint> endpointList = os.identity().serviceEndpoints().listEndpoints();
//        endpointList.forEach(item->{
//
//        });
//        OSClientV3 os = authenticateUnscoped(endpointResolver,"http://10.0.0.128:5000/v3","1b9116c74d7444768df7b23aa6cbf0d9","admin");
//        HypervisorStatistics hypervisorStatistics =os.compute().hypervisors().statistics();
//        System.out.println(hypervisorStatistics);
        System.out.println();
    }
}
