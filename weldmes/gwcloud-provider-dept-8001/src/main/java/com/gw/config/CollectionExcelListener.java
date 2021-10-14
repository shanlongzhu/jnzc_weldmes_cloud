package com.gw.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.gw.entities.MachineGatherInfo;
import com.gw.equipment.collection.service.CollectionService;

/**
 * @Author zhanghan
 * @Date 2021/10/14 10:40
 * @Description 采集模块导入监听器
 */
public class CollectionExcelListener extends AnalysisEventListener<MachineGatherInfo> {

    CollectionService collectionService;

    @Override
    public void invoke(MachineGatherInfo data, AnalysisContext analysisContext) {

        //将每条信息进行码值转换
        MachineGatherInfo machineGatherInfo = collectionService.importExcel(data);

        //进行批量入库
        collectionService.addMachineGatherInfos(machineGatherInfo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public CollectionExcelListener(CollectionService collectionService){

        this.collectionService = collectionService;
    }
}
