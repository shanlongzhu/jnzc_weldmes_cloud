package com.gw.data.team.service.impl;
import com.gw.data.team.dao.TeamDao;
import com.gw.data.team.service.TeamService;
import com.gw.entities.SysDept;
import com.gw.entities.UserLoginInfo;
import com.gw.entities.WeldStatisticsData;
import com.gw.sys.dao.SysDeptDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamDao teamDao;

    @Autowired
    SysDeptDao sysDeptDao;


    @Override
    public List<WeldStatisticsData> getList(String time1, String time2) {

        //如果传入时间 time1 为空
        if (ObjectUtils.isEmpty(time1)){

            List<WeldStatisticsData> list = new ArrayList<>();

            return list;

        }

        //如果传入 时间 均为空
        if (ObjectUtils.isEmpty(time1) && ObjectUtils.isEmpty(time2)){

            List<WeldStatisticsData> list = new ArrayList<>();

            return list;

        }

        Subject currentUser = SecurityUtils.getSubject();

        //获取到当前登录用户信息
        UserLoginInfo subject = (UserLoginInfo)currentUser.getPrincipal();

        if(ObjectUtils.isEmpty(subject.getDeptId())){

            //部门id为空
            return null;
        }

        //获取到当前用户部门id
        Long deptId = subject.getDeptId();

        //获取当前用户 所在部门的  下一级所有部门信息
        List<SysDept> sysDeptInfos = sysDeptDao.selectDeptInfosByParentId(deptId);

        List<WeldStatisticsData> weldStatisticsDataList = new ArrayList<>();

        do{

            List<SysDept> nextSysDeptInfos = new ArrayList<>();

            for (SysDept sysDeptInfo : sysDeptInfos) {

                //获取 当前用户所在部门 的 下级部门
                List<SysDept> sysDeptList = sysDeptDao.selectDeptInfosByParentId(sysDeptInfo.getId());

                nextSysDeptInfos.addAll(sysDeptList);
            }


            if (ObjectUtils.isEmpty(nextSysDeptInfos)){

                //执行班组生产数据报表查询   处理 班组层级 的部门信息
                weldStatisticsDataList = getGradeInfo(time1,time2,sysDeptInfos);

                return weldStatisticsDataList;
            }

            sysDeptInfos.clear();

            sysDeptInfos = nextSysDeptInfos;

        }while(!ObjectUtils.isEmpty(sysDeptInfos));

        return weldStatisticsDataList;
    }


    /**
     * @Date 2021/7/13 17:33
     * @Description  查询班组生产数据
     * @Params
     */
    public List<WeldStatisticsData> getGradeInfo(String time1,String time2,List<SysDept> list){
        List<Long> ids = new ArrayList<>();

        for (SysDept sysInfo : list) {
            Long id = sysInfo.getId();
            ids.add(id);
        }
        //执行班组生产数据报表查询
        List<WeldStatisticsData> weldStatisticsDataList = teamDao.getList(time1,time2,ids);

        return weldStatisticsDataList;
    }
}
