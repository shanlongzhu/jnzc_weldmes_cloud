package com.gw.process.craft.service.impl;

import com.gw.common.DateTimeUtils;
import com.gw.entities.MachineWeldsxInfo;
import com.gw.process.craft.dao.MachineWeldsxDao;
import com.gw.process.craft.service.MachineWeldsxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineWeldsxServiceImpl implements MachineWeldsxService {

    @Autowired
    MachineWeldsxDao machineWeldsxDao;

    /**
     * @Date 2021/8/25 15:32
     * @Description 松下设备列表查询
     * @Params
     */
    @Override
    public List<MachineWeldsxInfo> getMachineWeldsxInfos(Long equipType) {

        List<MachineWeldsxInfo> list = machineWeldsxDao.selectMachineWeldsxInfos(equipType);

        return list;
    }

    /**
     * @Date 2021/8/25 16:23
     * @Description 松下设备信息新增
     * @Params
     */
    @Override
    public void addMachineWeldsxInfo(MachineWeldsxInfo machineWeldsxInfo) {

        //获取当前系统时间
        String createTime = DateTimeUtils.getNowDateTime();

        machineWeldsxInfo.setCreateTime(createTime);

        machineWeldsxDao.insertMachineWeldsxInfo(machineWeldsxInfo);

    }

    /**
     * @Date 2021/8/25 16:37
     * @Description 根据 id  查询 松下设备信息
     * @Params
     */
    @Override
    public MachineWeldsxInfo getMachineWeldsxInfoById(Long id) {

        MachineWeldsxInfo machineWeldsxInfo = machineWeldsxDao.selectMachineWeldsxInfoById(id);

        return machineWeldsxInfo;
    }

    /**
     * @Date 2021/8/25 16:45
     * @Description 修改松下设备信息
     * @Params
     */
    @Override
    public void updateMachineWeldsxInfo(MachineWeldsxInfo machineWeldsxInfo) {

        machineWeldsxDao.updateMachineWeldsxInfo(machineWeldsxInfo);

    }

    /**
     * @Date 2021/8/25 16:59
     * @Description 删除松下设备信息
     * @Params
     */
    @Override
    public void delMachineWeldsxInfo(Long id) {

        machineWeldsxDao.deleteMachineWeldsxInfoById(id);
    }

    @Override
    public MachineWeldsxInfo getSxWeldCidById(Long id) {
        return machineWeldsxDao.getSxWeldCidById(id);
    }

}
