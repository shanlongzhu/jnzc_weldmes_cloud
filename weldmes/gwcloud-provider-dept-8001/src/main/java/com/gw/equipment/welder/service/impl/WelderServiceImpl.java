package com.gw.equipment.welder.service.impl;


import com.gw.common.ConstantInfo;
import com.gw.entities.*;
import com.gw.equipment.welder.dao.WelderDao;
import com.gw.equipment.welder.service.WelderService;
import com.gw.process.dispatch.dao.DispatchDao;
import com.gw.process.dispatch.dao.TaskClaimDao;
import com.gw.process.dispatch.service.TaskClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class WelderServiceImpl implements WelderService {

    @Autowired
    private WelderDao welderDao;

    @Autowired
    TaskClaimDao taskClaimDao;

    @Autowired
    TaskClaimService taskClaimService;


    @Override
    public List<MachineWeldInfo> getList(String machineNo,Integer type,Integer grade,Integer status,
                                         Integer firm,Long isNetwork,String gatherNo,String ipPath,Integer model,Integer area,Integer bay) {

        List<MachineWeldInfo> list = welderDao.getList(machineNo,type,grade,status,firm,isNetwork,gatherNo,ipPath,model,area,bay);

        for (MachineWeldInfo machineWeldInfo : list) {

            //判断焊机中是否有绑定的任务
            List<TaskClaim> taskClaims =taskClaimDao.selectTaskClaimInfoById(machineWeldInfo.getId());

            if(taskClaims.size() != 0){

                WeldClaimTaskInfo weldClaimTaskInfo = taskClaimService.getWeldClaimTaskInfo(taskClaims.get(0).getWeldId());

                if(!ObjectUtils.isEmpty(weldClaimTaskInfo)){

                    machineWeldInfo.setTaskFlag(ConstantInfo.WELD_EXIST_FLAG);
                    continue;
                }

                machineWeldInfo.setTaskFlag(ConstantInfo.WELD_NO_EXIST_FLAG);
            }
        }

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

    /**
     * @Date 2021/7/13 18:01
     * @Description 获取历史曲线中焊机id以及设备编号
     * @Params
     */
    @Override
    public List<MachineWeldInfo> getIdAndMachineNoOfWelderInfos() {

        List<MachineWeldInfo> list = welderDao.selectIdAndMachineNoOfWelderInfos();

        return list;
    }

}
