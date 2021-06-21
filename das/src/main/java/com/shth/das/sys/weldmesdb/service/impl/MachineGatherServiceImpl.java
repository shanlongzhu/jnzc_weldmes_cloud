package com.shth.das.sys.weldmesdb.service.impl;

import com.shth.das.pojo.GatherModel;
import com.shth.das.sys.weldmesdb.mapper.MachineGatherMapper;
import com.shth.das.sys.weldmesdb.service.MachineGatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "ds1TransactionManager")
public class MachineGatherServiceImpl implements MachineGatherService {

    @Autowired
    MachineGatherMapper gatherMapper;

    @Override
    public List<GatherModel> getMachineGatherAll() {
        return gatherMapper.selectList(null);
    }

}
