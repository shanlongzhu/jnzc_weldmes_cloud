package com.gw.data.team.service.impl;

import com.gw.common.ConstantInfo;
import com.gw.common.DateTimeUtils;
import com.gw.data.team.dao.TeamDao;
import com.gw.data.team.service.TeamService;
import com.gw.entities.WeldStatisticsDataTeam;
import com.gw.sys.dao.SysDeptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Date 2021/10/19 14:25
 * @Description 班组报表业务实现层
 * @Params
 */
@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamDao teamDao;

    @Autowired
    SysDeptDao sysDeptDao;

    /**
     * @Date 2021/10/19 14:26
     * @Description 获取班组生产数据列表
     * @Params
     */
    @Override
    public List<WeldStatisticsDataTeam> getList(String time1, String time2, List<Long> ids) {

        List<WeldStatisticsDataTeam> weldStatisticsDataList = new ArrayList<>();

        //如果传入 时间 均为空
        if (ObjectUtils.isEmpty(time1) && ObjectUtils.isEmpty(time2)) {

            return weldStatisticsDataList;

        }

        //终止时间为空时  设置为当前系统时间
        if (!ObjectUtils.isEmpty(time1) && ObjectUtils.isEmpty(time2)) {

            time2 = DateTimeUtils.getNowDateTime();
        }

        //查询班组报表信息
        weldStatisticsDataList = teamDao.getList(time1, time2, ids);

        //获取time2的日期
        String date = time2.split(" ")[0];

        //获取time2的时间
        String nowTime = time2.split(" ")[1];

        //日期格式 yyyy-MM-dd 替换为 yyyyMMdd
        String tableDate = date.split("-")[0] + date.split("-")[1] + date.split("-")[2];

        //获取OTC表名
        String otcTableName = ConstantInfo.OTC_TABLE_NAME_PREFIX_FLAG + tableDate;

        //获取松下表名
        String sxTableName = ConstantInfo.SX_TABLE_NAME_PREFIX_FLAG + tableDate;

        //获取查询实时表的整点时间
        String time = date + " " + nowTime.split(":")[0]+ ":00:00";

        List<WeldStatisticsDataTeam> realTimeInfos = teamDao.getOTCAndSXRealTimeInfos(otcTableName,sxTableName,
                time,time2,ids);

        weldStatisticsDataList.addAll(realTimeInfos);

        Set<WeldStatisticsDataTeam> set = new HashSet<>(weldStatisticsDataList);

        List<WeldStatisticsDataTeam> asList = new ArrayList<>(set);

        return asList;
    }

    /**
     * @Date 2021/10/21 9:41
     * @Description 通过组织机构id 查询 该部门下所有的班组id
     * @Params
     */
    @Override
    public List<Long> getNextDeptIds(String deptId) {

        //通过组织机构id 查询 该部门下所有的班组id
        List<Long> ids = sysDeptDao.selectNextDeptIdsById(deptId);

        return ids;
    }

}
