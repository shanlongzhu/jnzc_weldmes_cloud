package com.shth.das.sys.weldmesdb.service.impl;

import com.shth.das.pojo.WeldModel;
import com.shth.das.sys.weldmesdb.mapper.MachineWeldMapper;
import com.shth.das.sys.weldmesdb.service.MachineWeldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "ds1TransactionManager", rollbackFor = Exception.class)
public class MachineWeldServiceImpl implements MachineWeldService {

    @Autowired
    MachineWeldMapper weldMapper;


    @Override
    public List<WeldModel> getMachineWeldAll() {
        return weldMapper.getMachineWeldAll();
    }
}
