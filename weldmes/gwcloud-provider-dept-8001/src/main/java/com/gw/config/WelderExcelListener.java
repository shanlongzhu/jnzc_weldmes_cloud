package com.gw.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.gw.entities.MachineWeldInfo;
import com.gw.entities.WelderInfo;
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

    @Override
    public void invoke(MachineWeldInfo data, AnalysisContext analysisContext) {

        //将每条信息进行码值转换
        MachineWeldInfo machineWeldInfo = welderService.importExcel(data);

        list.add(machineWeldInfo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        //进行批量入库
        welderService.addMachineWeldInfos(list);

        //入库完成 清理 list
        list.clear();

    }

    public WelderExcelListener(WelderService welderService){

        this.welderService = welderService;
    }
}
