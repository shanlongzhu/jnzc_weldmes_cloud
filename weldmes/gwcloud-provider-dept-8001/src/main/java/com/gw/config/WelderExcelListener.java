package com.gw.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.gw.entities.MachineWeldInfo;
import com.gw.equipment.welder.service.WelderService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/10/13 16:22
 * @Description 焊机管理导入监听器
 */
public class WelderExcelListener extends AnalysisEventListener<MachineWeldInfo> {

    WelderService welderService;

    List<MachineWeldInfo> list = new ArrayList<>();

    public WelderExcelListener(WelderService welderService){

        this.welderService = welderService;
    }

    @Override
    public void invoke(MachineWeldInfo data, AnalysisContext analysisContext) {

        MachineWeldInfo machineWeldInfo = welderService.importExcel(data);

        list.add(machineWeldInfo);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        welderService.addMachineWeldInfos(list);

        list.clear();

    }
}
