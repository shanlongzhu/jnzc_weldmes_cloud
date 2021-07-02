package com.gw.controller;

import com.gw.entities.Dept;
import com.gw.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    @Qualifier("discoveryClient")
    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/dept/add", method = RequestMethod.POST)
    public boolean add(Dept dept) {
        return deptService.add(dept);
    }

    @RequestMapping(value="/dept/get/{id}",method=RequestMethod.GET)
    @HystrixCommand(fallbackMethod="processHystrix_Get")
    public Dept get(@PathVariable("id")Long id) {
        Dept dept=deptService.get(id);
        //故意出错
        if(dept==null) {
            throw new RuntimeException("该id没有对应的信息");
        }
        return dept;
    }

    public Dept processHystrix_Get(@PathVariable("id")Long id) {
        return new Dept().setDeptno(id).setDname("该id没有对应的信息").
                setDb_source("no this database in mysql");
    }

    @RequestMapping(value = "/dept/list", method = RequestMethod.GET)
    public List<Dept> list() {
        return deptService.list();
    }

    @RequestMapping(value="/dept/discovery",method=RequestMethod.GET)
    public Object discovery() {
        List<String> list=client.getServices();//查看eureka中一共有多少个微服务
        System.out.println("============"+list);
        List<ServiceInstance> serList=client.getInstances("GWCLOUD-DEPT");
        for (ServiceInstance element : serList) {
            System.out.println(element.getServiceId()+"\t"+element.getHost());

        }
        return this.client;
    }
}
