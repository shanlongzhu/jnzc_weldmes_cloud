package com.gw.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.gw.entities.SysDictionary;
import com.gw.entities.TaskInfo;
import com.gw.process.dispatch.dao.DispatchDao;
import com.gw.sys.dao.SysDictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/10/9 15:33
 * @Description
 */
public class ExcelListener extends AnalysisEventListener<TaskInfo> {

    @Autowired
    private DispatchDao dispatchDao;

    @Autowired
    private SysDictionaryDao sysDictionaryDao;

    List<SysDictionary> dictionaryInfos = sysDictionaryDao.selectDictionaryInfos(null,null,null,null);

    //private static final ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    List<TaskInfo> list = new ArrayList();

    @Override
    public void invoke(TaskInfo data, AnalysisContext analysisContext) {



        list.add(data);

        dispatchDao.addTaskInfoList(list);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    public ExcelListener(DispatchDao dispatchDao) {

        this.dispatchDao = dispatchDao;
    }
}
