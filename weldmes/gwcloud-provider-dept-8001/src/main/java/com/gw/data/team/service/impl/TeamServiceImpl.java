package com.gw.data.team.service.impl;

import com.gw.common.DateTimeUtils;
import com.gw.data.team.dao.TeamDao;
import com.gw.data.team.service.TeamService;
import com.gw.entities.UserLoginInfo;
import com.gw.entities.WeldStatisticsDataTeam;
import com.gw.sys.dao.SysDeptDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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

        return weldStatisticsDataList;
    }

    /**
     * @Date 2021/10/21 9:41
     * @Description 通过组织机构id 查询 该部门下所有的班组id
     * @Params
     */
    @Override
    public List<Long> getNextDeptIds(String deptId) {

        //判断用户部门id是否传入
        if(ObjectUtils.isEmpty(deptId)){

            //获取到当前用户
            Subject currentUser = SecurityUtils.getSubject();

            UserLoginInfo subject = (UserLoginInfo)currentUser.getPrincipal();

            deptId = subject.getDeptId().toString();

        }

        //通过组织机构id 查询 该部门下所有的班组id
        List<Long> ids = sysDeptDao.selectNextDeptIdsById(deptId);

        return ids;
    }

}
