package com.shth.das.sys.weldmesdb.service;

import com.shth.das.pojo.db.TaskModel;

import java.util.List;

public interface TaskService {

    /**
     * 查询所有任务信息不分页
     * @return
     */
    List<TaskModel> getTaskModelAll();
}
