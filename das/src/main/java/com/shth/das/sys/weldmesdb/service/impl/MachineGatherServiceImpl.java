package com.shth.das.sys.weldmesdb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shth.das.pojo.db.GatherModel;
import com.shth.das.sys.weldmesdb.mapper.MachineGatherMapper;
import com.shth.das.sys.weldmesdb.service.MachineGatherService;
import com.shth.das.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "ds1TransactionManager", rollbackFor = Exception.class)
@Slf4j
public class MachineGatherServiceImpl implements MachineGatherService {

    @Autowired
    MachineGatherMapper gatherMapper;

    @Override
    public List<GatherModel> getMachineGatherAll() {
        return gatherMapper.selectList(null);
    }

    @Override
    public void updateGatherIpByNumber(String gatherNo, String weldIp) {
        try {
            gatherNo = CommonUtils.stringLengthJoint(gatherNo, 4);
            GatherModel gatherModel = new GatherModel();
            gatherModel.setIpPath(weldIp);
            gatherMapper.update(gatherModel, new QueryWrapper<GatherModel>().eq("gather_no", gatherNo));
        } catch (Exception e) {
            log.error("OTC设备根据采集编号更新IP异常：", e);
            throw new RuntimeException();
        }
    }

}
