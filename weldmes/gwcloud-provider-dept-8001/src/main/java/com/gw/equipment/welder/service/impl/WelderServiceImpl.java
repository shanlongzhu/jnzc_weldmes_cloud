package com.gw.equipment.welder.service.impl;

import com.gw.common.ConstantInfo;
import com.gw.common.DateTimeUtils;
import com.gw.entities.EquipFeatureInfo;
import com.gw.entities.MachineWeldInfo;
import com.gw.entities.SysDept;
import com.gw.equipment.welder.dao.WelderDao;
import com.gw.equipment.welder.service.WelderService;
import com.gw.process.dispatch.dao.TaskClaimDao;
import com.gw.process.dispatch.service.TaskClaimService;
import com.gw.sys.dao.SysDeptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2021/10/14 13:55
 * @Description 焊机管理业务实现层
 * @Params
 */
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


    /**
     * @Date 2021/10/13 17:18
     * @Description 焊机信息列表查询
     * @Params
     */
    @Override
    public List<MachineWeldInfo> getList(String machineNo, Integer type, Integer grade, Integer status,
                                         Integer firm, Long isNetwork, String gatherNo, String ipPath, Integer model, Integer area, Integer bay) {

        List<MachineWeldInfo> list = welderDao.selectMachineWeldInfos(machineNo, type, grade, status, firm, isNetwork, gatherNo, ipPath, model, area, bay);

        return list;
    }

    /**
     * @Date 2021/10/13 17:19
     * @Description 添加焊机信息
     * @Params
     */
    @Override
    public void addWelder(MachineWeldInfo machineWeldInfo) {

        welderDao.addWelder(machineWeldInfo);

    }

    /**
     * @Date 2021/10/13 17:21
     * @Description 根据焊机id 查询 焊机信息
     * @Params
     */
    @Override
    public List<MachineWeldInfo> getById(Long id) {

        List<MachineWeldInfo> list = welderDao.getById(id);

        return list;
    }

    /**
     * @Date 2021/10/13 17:21
     * @Description 修改焊机信息
     * @Params
     */
    @Override
    public void updateWelder(MachineWeldInfo machineWeldInfo) {

        welderDao.updateWelder(machineWeldInfo);
    }

    /**
     * @Date 2021/10/13 17:21
     * @Description 删除焊机信息
     * @Params
     */
    @Override
    public void deleteWelder(Long id) {

        welderDao.deleteWelder(id);
    }

    @Override
    public Long getDeptId(String deptName) {

        Long deptId = welderDao.getDeptId(deptName);

        return deptId;
    }

    /**
     * @Date 2021/8/18 17:43
     * @Description 对焊机信息进行码值转换
     * @Params
     */
    @Override
    public MachineWeldInfo importExcel(MachineWeldInfo data) {

        //设备状态
        if (!ObjectUtils.isEmpty(data.getStatusStr())) {

            Byte statusId = welderDao.getStatusId(data.getStatusStr());

            if (!ObjectUtils.isEmpty(statusId)) {

                data.setStatus(statusId);
            }
        }

        //设备类型
        if (!ObjectUtils.isEmpty(data.getTypeStr())) {

            Byte typeId = welderDao.getTypeId(data.getTypeStr(), ConstantInfo.DICTIONARY_WELD_TYPE_FLAG);

            if (!ObjectUtils.isEmpty(typeId)) {

                data.setType(typeId);
            }
        }

        //厂商
        if (!ObjectUtils.isEmpty(data.getFirmStr())) {

            Byte firmId = welderDao.getFirmId(data.getFirmStr());

            if (!ObjectUtils.isEmpty(firmId)) {

                data.setFirm(firmId);
            }
        }

        //设备型号
        if (!ObjectUtils.isEmpty(data.getModelStr())) {

            Byte modelId = welderDao.getModelId(data.getModelStr());

            if (!ObjectUtils.isEmpty(modelId)) {

                data.setModel(modelId);
            }
        }

        //机构ID
        if (!ObjectUtils.isEmpty(data.getDeptName())) {

            Long deptId = welderDao.getDeptId(data.getDeptName());

            if (!ObjectUtils.isEmpty(deptId)) {

                data.setDeptId(deptId);
            }
        }

        //采集序号
        if (!ObjectUtils.isEmpty(data.getGatherNo())) {

            Long gatherId = welderDao.getGid(data.getGatherNo());

            if (!ObjectUtils.isEmpty(gatherId)) {

                data.setGId(gatherId);
            }
        }

        //区域
        if (!ObjectUtils.isEmpty(data.getAreaStr())) {

            Byte area = welderDao.getTypeId(data.getAreaStr(), ConstantInfo.AREA_FLAG);

            if(!ObjectUtils.isEmpty(area)){

                Long areaId = area.longValue();

                data.setArea(areaId);
            }
        }

        //跨间
        if (!ObjectUtils.isEmpty(data.getBayStr())) {

            Byte bay = welderDao.getTypeId(data.getBayStr(), ConstantInfo.BAY_FLAG);

            if (!ObjectUtils.isEmpty(bay)) {

                data.setBay(bay.longValue());
            }
        }

        return data;

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
     * @Description 根据部门id查询设备信息列表
     * @Params id 部门id
     */
    @Override
    public List<MachineWeldInfo> getWeldInfos(Long id) {

        //获取当前用户 所在部门的  下一级所有部门信息
        List<SysDept> sysDeptInfos = sysDeptDao.selectDeptInfosByParentId(id);

        List<Long> deptIds = new ArrayList<>();

        List<MachineWeldInfo> list = new ArrayList<>();

        if (ObjectUtils.isEmpty(sysDeptInfos)) {

            //当前部门id为班组层级  查询该班组下的设备信息列表
            deptIds.add(id);

            //根据部门id列表查询设备信息
            list = welderDao.selectMachineWeldInfosByDeptIds(deptIds);

            return list;
        }

        do {

            List<SysDept> nextSysDeptInfos = new ArrayList<>();

            for (SysDept sysDeptInfo : sysDeptInfos) {

                //获取 当前用户所在部门 的 下级部门
                List<SysDept> sysDeptList = sysDeptDao.selectDeptInfosByParentId(sysDeptInfo.getId());

                nextSysDeptInfos.addAll(sysDeptList);
            }

            if (ObjectUtils.isEmpty(nextSysDeptInfos)) {

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

        } while (!ObjectUtils.isEmpty(sysDeptInfos));

        return list;
    }

    /**
     * @Date 2021/10/12 15:35
     * @Description 焊机是否绑定任务列表查询
     * @Params
     */
    @Override
    public List<MachineWeldInfo> getStatusOfMachineWeldInfos(String machineNo, Integer type, Integer grade, Integer status, Integer firm, Long isNetwork, String gatherNo, String ipPath, Integer model, Integer area, Integer bay) {

        //获取当天早上7点的时间
        String time1 = DateTimeUtils.getNowDateTime();

        time1 = time1.split(" ")[0] + " 07:00:00";

        //获取次日当前系统时间
        String time2 = DateTimeUtils.getNowSecondDateTime();

        time2 = time2.split(" ")[0] + " 07:00:00";

        List<MachineWeldInfo> list = welderDao.getStatusOfMachineWeldInfos(machineNo, type, grade, status, firm, isNetwork,
                gatherNo, ipPath, model, area, bay, time1, time2);

        return list;
    }

    /**
     * @Date 2021/10/13 17:16
     * @Description 批量插入焊机信息
     * @Params
     */
    @Override
    public void addMachineWeldInfos(List<MachineWeldInfo> list) {

        welderDao.insertMachineWeldInfoByGroup(list);

    }

    /**
     * @Date 2021/10/22 9:23
     * @Description 根据焊机标识、焊机id 获取焊机特征信息
     * @Params
     */
    @Override
    public EquipFeatureInfo getEquipFeatureInfo(Integer macFlag, Long id) {

        EquipFeatureInfo equipFeatureInfo;

        //判断焊机类型  0:OTC  1:松下
        if(macFlag == ConstantInfo.MACHINE_TYPE_FLAG){

            //获取OTC焊机特征信息
            equipFeatureInfo = welderDao.selectOTCEquipFeatureInfo(macFlag,id);

            //获取OTC焊机 焊接时长
            String weldDuration = welderDao.selectOTCMachineWeldDuration(id);

            equipFeatureInfo.setWeldingDuration(weldDuration);

        }else{

            //获取松下设备CID
            String weldCid = id.toString();

            //获取松下焊机特征信息
            equipFeatureInfo = welderDao.selectSXEquipFeatureInfo(macFlag,weldCid);

            //获取松下焊机 焊接时长
            String weldDuration = welderDao.selectSXMachineWeldDuration(weldCid);

            equipFeatureInfo.setWeldingDuration(weldDuration);
        }

        return equipFeatureInfo;
    }

}
