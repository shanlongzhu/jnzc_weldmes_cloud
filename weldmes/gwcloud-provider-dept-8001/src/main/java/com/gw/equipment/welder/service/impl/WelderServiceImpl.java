package com.gw.equipment.welder.service.impl;


import com.gw.common.ConstantInfo;
import com.gw.entities.*;
import com.gw.equipment.welder.dao.WelderDao;
import com.gw.equipment.welder.service.WelderService;
import com.gw.process.dispatch.dao.DispatchDao;
import com.gw.process.dispatch.dao.TaskClaimDao;
import com.gw.process.dispatch.service.TaskClaimService;
import com.gw.sys.dao.SysDeptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class WelderServiceImpl implements WelderService {

    @Autowired
    private WelderDao welderDao;

    @Autowired
    TaskClaimDao taskClaimDao;

    @Autowired
    TaskClaimService taskClaimService;

    @Autowired
    SysDeptDao sysDeptDao;


    @Override
    public List<MachineWeldInfo> getList(String machineNo,Integer type,Integer grade,Integer status,
                                         Integer firm,Long isNetwork,String gatherNo,String ipPath,Integer model,Integer area,Integer bay) {

        List<MachineWeldInfo> list = welderDao.selectMachineWeldInfosNoPage(machineNo,type,grade,status,firm,isNetwork,gatherNo,ipPath,model,area,bay);

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
    public Byte getTypeId(String type,String dictionaryType) {
        return welderDao.getTypeId(type,dictionaryType);
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

    /**
     * @Date 2021/7/29 13:20
     * @Description  根据部门id查询设备信息列表
     * @Params id 部门id
     */
    @Override
    public List<MachineWeldInfo> getWeldInfos(Long id) {

        //获取当前用户 所在部门的  下一级所有部门信息
        List<SysDept> sysDeptInfos = sysDeptDao.selectDeptInfosByParentId(id);

        List<Long> deptIds = new ArrayList<>();

        List<MachineWeldInfo> list = new ArrayList<>();

        if (ObjectUtils.isEmpty(sysDeptInfos)){

            //当前部门id为班组层级  查询该班组下的设备信息列表
            deptIds.add(id);

            //根据部门id列表查询设备信息
            list = welderDao.selectMachineWeldInfosByDeptIds(deptIds);

            return list;
        }

        do{

            List<SysDept> nextSysDeptInfos = new ArrayList<>();

            for (SysDept sysDeptInfo : sysDeptInfos) {

                //获取 当前用户所在部门 的 下级部门
                List<SysDept> sysDeptList = sysDeptDao.selectDeptInfosByParentId(sysDeptInfo.getId());

                nextSysDeptInfos.addAll(sysDeptList);
            }

            if (ObjectUtils.isEmpty(nextSysDeptInfos)){

                //当前部门id为班组层级  查询该班组下的设备信息列表
                for (SysDept sysDept : sysDeptInfos) {

                    deptIds.add(sysDept.getId());

                }

                //根据部门id列表查询设备信息
                list = welderDao.selectMachineWeldInfosByDeptIds(deptIds);

                return list;
            }

            sysDeptInfos.clear();

            sysDeptInfos = nextSysDeptInfos;

        }while(!ObjectUtils.isEmpty(sysDeptInfos));

        return list;
    }

}
