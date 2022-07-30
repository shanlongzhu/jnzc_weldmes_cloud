package com.gw.equipment.collection.service.impl;


import com.gw.common.DateTimeUtils;
import com.gw.entities.MachineGatherInfo;
import com.gw.equipment.collection.dao.CollectionDao;
import com.gw.equipment.collection.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionDao collectionDao;

    /**
     * @Date 2021/10/14 9:53
     * @Description 采集模块列表查询
     * @Params
     */
    @Override
    public List<MachineGatherInfo> getList(Integer grade, Integer gatherNo) {

        List<MachineGatherInfo> list = collectionDao.getList(grade, gatherNo);

        return list;

    }

    /**
     * @Date 2021/10/14 9:54
     * @Description 添加采集信息
     * @Params
     */
    @Override
    public void addCollection(MachineGatherInfo machineGatherInfo) {

        //获取当前时间
        String time = DateTimeUtils.getNowDateTime();

        machineGatherInfo.setCreateTime(time);

        collectionDao.addCollection(machineGatherInfo);
    }

    /**
     * @Date 2021/10/14 9:54
     * @Description 删除采集信息
     * @Params
     */
    @Override
    public void deleteCollection(long id) {

        collectionDao.deleteCollection(id);
    }

    /**
     * @Date 2021/10/14 9:54
     * @Description 根据id 查询 采集信息
     * @Params
     */
    @Override
    public List<MachineGatherInfo> getById(Long id) {

        List<MachineGatherInfo> list = collectionDao.getById(id);

        return list;
    }

    /**
     * @Date 2021/10/14 9:54
     * @Description 修改采集信息
     * @Params
     */
    @Override
    public void updateCollection(MachineGatherInfo machineGatherInfo) {

        collectionDao.updateCollection(machineGatherInfo);
    }

    /**
     * @Date 2021/10/14 9:55
     * @Description 采集信息码值转换
     * @Params
     */
    @Override
    public MachineGatherInfo importExcel(MachineGatherInfo data) {

        //所属项目
        if(!ObjectUtils.isEmpty(data.getDeptName())){

            Long deptId = collectionDao.getDeptId(data.getDeptName());

            if(!ObjectUtils.isEmpty(deptId)){

                data.setDeptId(deptId);
            }
        }

        //状态
        if(!ObjectUtils.isEmpty(data.getStatusStr())){

            Integer status = collectionDao.getStatus(data.getStatusStr());

            if(!ObjectUtils.isEmpty(status)){

                data.setStatus(status);
            }
        }

        //协议
        if(!ObjectUtils.isEmpty(data.getProtocolStr())){

            Integer protocol = collectionDao.getStatus(data.getProtocolStr());

            if(!ObjectUtils.isEmpty(protocol)){

                data.setProtocol(protocol);
            }
        }

        return data;

    }

    /**
     * @Date 2021/10/14 9:55
     * @Description 根据部门名称查询 部门id
     * @Params
     */
    @Override
    public Long getDeptId(String name) {

        Long deptId = collectionDao.getDeptId(name);

        return deptId;
    }

    /**
     * @Date 2021/7/1 9:05
     * @Description 获取采集序号列表
     * @Params
     */
    @Override
    public List<MachineGatherInfo> queryGatherNos() {

        List<MachineGatherInfo> list = collectionDao.queryGatherNos();

        return list;
    }

    /**
     * @Date 2021/10/14 11:05
     * @Description 批量插入采集信息
     * @Params
     */
    @Override
    public void addMachineGatherInfos(MachineGatherInfo machineGatherInfo) {

        collectionDao.addOrUpdateMachineGatherInfos(machineGatherInfo);

    }

    @Override
    public List<MachineGatherInfo> getGatherNoByWeldId(Long weldId) {
        return collectionDao.getGatherNoByWeldId(weldId);
    }

}
