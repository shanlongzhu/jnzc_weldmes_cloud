package com.gw.equipment.welder.service.impl;


import com.gw.entities.MachineGatherInfo;
import com.gw.entities.MachineWeldInfo;
import com.gw.equipment.welder.dao.WelderDao;
import com.gw.equipment.welder.service.WelderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WelderServiceImpl implements WelderService {

    @Autowired
    private WelderDao welderDao;


    @Override
    public List<MachineWeldInfo> getList(String machineNo,Integer type,Integer grade,Integer status,
                                         Integer firm,Long isNetwork,String gatherNo,String ipPath,Integer model) {
        List<MachineWeldInfo> list=welderDao.getList(machineNo,type,grade,status,firm,isNetwork,gatherNo,ipPath,model);
        return list;
    }

    @Override
    public int addWelder(MachineWeldInfo machineWeldInfo) {
        return welderDao.addWelder(machineWeldInfo);

    }

    @Override
    public List<MachineWeldInfo> getById(Long id) {
        return welderDao.getById(id);
    }

    @Override
    public int updateWelder(MachineWeldInfo machineWeldInfo) {
        return welderDao.updateWelder(machineWeldInfo);
    }

    @Override
    public int deleteWelder(Long id) {
        return welderDao.deleteWelder(id);
    }

    @Override
    public Byte getTypeId(String type) {
        return welderDao.getTypeId(type);
    }

    @Override
    public Long getDeptId(String deptName) {
        return welderDao.getDeptId(deptName);
    }

    @Override
    public Byte getStatusId(String status) {
        return welderDao.getStatusId(status);
    }

    @Override
    public Byte getFirmId(String firm) {
        return welderDao.getFirmId(firm);
    }

    @Override
    public Long getGid(String machineNo) {
        return welderDao.getGid(machineNo);
    }

    @Override
    public Byte getModelId(String model) {
        return welderDao.getModelId(model);
    }

    @Override
    public void importExcel(List<MachineWeldInfo> machineWeldInfoArrayList) {
        for (MachineWeldInfo machineWeldInfo : machineWeldInfoArrayList) {
            welderDao.save(machineWeldInfo);
        }
    }

}
