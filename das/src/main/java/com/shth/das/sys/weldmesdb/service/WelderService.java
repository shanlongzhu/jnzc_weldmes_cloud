package com.shth.das.sys.weldmesdb.service;

import com.shth.das.pojo.WelderModel;

import java.util.List;

public interface WelderService {

    /**
     * 查询所有焊工信息不分页
     * @return
     */
    List<WelderModel> getWelderModelAll();
}
