package com.gw.equipment.collection.service.impl;



import com.gw.common.DateTimeUtil;
import com.gw.entities.MachineGatherInfo;
import com.gw.equipment.collection.dao.CollectionDao;
import com.gw.equipment.collection.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionDao collectionDao;

    @Override
    public List<MachineGatherInfo> getList(Integer grade,Integer gatherNo) {
        List<MachineGatherInfo> list=collectionDao.getList(grade,gatherNo);
        return list;

    }

    @Override
    public void addCollection(MachineGatherInfo machineGatherInfo) {

        //获取当前时间
        String time = DateTimeUtil.getCurrentTime();

        machineGatherInfo.setCreateTime(time);

        collectionDao.addCollection(machineGatherInfo);
    }

    @Override
    public void deleteCollection(long id) {

        collectionDao.deleteCollection(id);
    }

    @Override
    public List<MachineGatherInfo> getById(Long id) {

        List<MachineGatherInfo> list=collectionDao.getById(id);
        return list;
    }

    @Override
    public void updateCollection(MachineGatherInfo machineGatherInfo) {

        collectionDao.updateCollection(machineGatherInfo);
    }

    @Override
    public void importExcel(List<MachineGatherInfo> machineGatherInfoList) {
        for (MachineGatherInfo machineGatherInfo : machineGatherInfoList) {
            collectionDao.save(machineGatherInfo);
        }
    }

    @Override
    public Long getDeptId(String name) {
        Long id=collectionDao.getDeptId(name);
        return id;
    }

    @Override
    public Integer getStatus(String valueName) {
        Integer status=collectionDao.getStatus(valueName);
        return status;
    }

    @Override
    public Integer getProtocol(String valueNames) {
        Integer protocol=collectionDao.getProtocol(valueNames);
        return protocol;
    }

    /**
     * @Date 2021/7/1 9:05
     * @Description  获取采集序号列表
     * @Params
     */
    @Override
    public List<MachineGatherInfo> queryGatherNos() {

        List<MachineGatherInfo> list = collectionDao.queryGatherNos();

        return list;
    }

}
