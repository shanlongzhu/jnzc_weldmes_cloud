package com.shth.das.sys.weldmesdb.service.impl;

import com.shth.das.pojo.db.TaskModel;
import com.shth.das.sys.weldmesdb.mapper.TaskMapper;
import com.shth.das.sys.weldmesdb.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "ds1TransactionManager", rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<TaskModel> getTaskModelAll() {
        return taskMapper.selectList(null);
    }
}
