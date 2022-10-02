package com.shth.das.sys.weldmesdb.service.impl;

import com.shth.das.pojo.db.WelderModel;
import com.shth.das.sys.weldmesdb.mapper.WelderMapper;
import com.shth.das.sys.weldmesdb.service.WelderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "ds1TransactionManager", rollbackFor = Exception.class)
public class WelderServiceImpl implements WelderService {

    @Autowired
    private WelderMapper welderMapper;

    @Override
    public List<WelderModel> getWelderModelAll() {
        return welderMapper.selectList(null);
    }
}
