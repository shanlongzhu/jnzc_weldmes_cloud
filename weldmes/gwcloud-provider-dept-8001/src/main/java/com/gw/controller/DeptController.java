package com.gw.controller;

import com.gw.entities.Dept;
import com.gw.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
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

//    public Dept processHystrix_Get(@PathVariable("id")Long id) {
//        return new Dept().setDeptno(id).setDname("该id没有对应的信息").
//                setDb_source("no this database in mysql");
//    }

    @RequestMapping(value = "/dept/list", method = RequestMethod.GET)
    public List<Dept> list() {
        return deptService.list();
    }


    @RequestMapping(value="/dept/discovery",method=RequestMethod.GET)
    public Object discovery() {
        List<String> list=client.getServices();//获取eureka中所有的已经注册的服务
        System.out.println("==========="+list);
        List<ServiceInstance> serList=client.getInstances("GWCLOUD-DEPT");//获取指定的实例
        System.out.println("serList.size(): "+serList.size());
        return this.client;
    }

}
