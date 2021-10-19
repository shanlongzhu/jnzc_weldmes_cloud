package com.gw.data.team.service.impl;

import com.gw.common.DateTimeUtils;
import com.gw.data.team.dao.TeamDao;
import com.gw.data.team.service.TeamService;
import com.gw.entities.SysDept;
import com.gw.entities.WeldStatisticsDataTeam;
import com.gw.sys.dao.SysDeptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

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
    public List<WeldStatisticsDataTeam> getList(String time1, String time2, String deptId) {

        List<WeldStatisticsDataTeam> weldStatisticsDataList = new ArrayList<>();

        //如果传入 时间 均为空
        if (ObjectUtils.isEmpty(time1) && ObjectUtils.isEmpty(time2)) {

            return weldStatisticsDataList;

        }

        //终止时间为空时  设置为当前系统时间
        if (!ObjectUtils.isEmpty(time1) && ObjectUtils.isEmpty(time2)) {

            time2 = DateTimeUtils.getNowDateTime();
        }

        //获取当前用户 所在部门的  下一级所有部门信息
        List<SysDept> sysDeptInfos = sysDeptDao.selectDeptInfosByParentId(Long.parseLong(deptId));

        if (ObjectUtils.isEmpty(sysDeptInfos)) {

            //执行班组生产数据报表查询   处理 班组层级 的部门信息
            weldStatisticsDataList = getGradeInfo(time1, time2, sysDeptInfos);

            return weldStatisticsDataList;
        }

        do {

            List<SysDept> nextSysDeptInfos = new ArrayList<>();

            for (SysDept sysDeptInfo : sysDeptInfos) {

                //获取 当前用户所在部门 的 下级部门
                List<SysDept> sysDeptList = sysDeptDao.selectDeptInfosByParentId(sysDeptInfo.getId());

                nextSysDeptInfos.addAll(sysDeptList);
            }

            if (ObjectUtils.isEmpty(nextSysDeptInfos)) {

                //执行班组生产数据报表查询   处理 班组层级 的部门信息
                weldStatisticsDataList = getGradeInfo(time1, time2, sysDeptInfos);

                return weldStatisticsDataList;
            }

            sysDeptInfos.clear();

            sysDeptInfos = nextSysDeptInfos;

        } while (!ObjectUtils.isEmpty(sysDeptInfos));

        return weldStatisticsDataList;
    }


    /**
     * @Date 2021/7/13 17:33
     * @Description 查询班组生产数据
     * @Params
     */
    public List<WeldStatisticsDataTeam> getGradeInfo(String time1, String time2, List<SysDept> list) {
        List<Long> ids = new ArrayList<>();

        for (SysDept sysInfo : list) {
            Long id = sysInfo.getId();
            ids.add(id);
        }
        //执行班组生产数据报表查询
        List<WeldStatisticsDataTeam> weldStatisticsDataList = teamDao.getList(time1, time2, ids);

        return weldStatisticsDataList;
    }

}
