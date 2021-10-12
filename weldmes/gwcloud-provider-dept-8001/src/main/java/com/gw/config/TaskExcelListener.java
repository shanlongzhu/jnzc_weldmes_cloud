package com.gw.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.gw.entities.TaskInfo;
import com.gw.process.dispatch.service.DispatchService;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/10/9 15:33
 * @Description
 */
public class TaskExcelListener extends AnalysisEventListener<TaskInfo> {

    DispatchService dispatchService;

    /**
     * 每隔10条存储数据库,然后清理list 方便内存回收
     */
    private static final int BATCH_COUNT = 10;

    List<TaskInfo> list = new ArrayList();

    @Override
    public void invoke(TaskInfo data, AnalysisContext analysisContext) {

        TaskInfo taskInfo = dispatchService.importExcel(data);

        list.add(taskInfo);

        if(list.size() == BATCH_COUNT){

            dispatchService.addTaskInfos(list);

            //存储完成 清理 list
            list.clear();

        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    public TaskExcelListener(DispatchService dispatchService) {

        this.dispatchService = dispatchService;
    }
}
