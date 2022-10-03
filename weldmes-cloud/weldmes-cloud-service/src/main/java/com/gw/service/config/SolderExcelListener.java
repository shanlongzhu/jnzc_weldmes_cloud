package com.gw.service.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.gw.entities.WelderInfo;
import com.gw.service.process.solderer.service.SoldererService;

import java.util.ArrayList;
import java.util.List;

public class SolderExcelListener extends AnalysisEventListener<WelderInfo> {

    SoldererService soldererService;

    List<WelderInfo> list = new ArrayList<>();

    @Override
    public void invoke(WelderInfo data, AnalysisContext analysisContext) {

        //将每条信息进行码值转换
        WelderInfo welderInfo = soldererService.importExcel(data);

        list.add(welderInfo);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        //进行批量入库
        soldererService.addWelderInfos(list);

        //入库完成 清理 list
        list.clear();

    }

    public SolderExcelListener(SoldererService soldererService) {

        this.soldererService = soldererService;

    }
}
