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

        //获取到当前登录用户信息
        Subject currentUser = SecurityUtils.getSubject();

        UserLoginInfo subject = (UserLoginInfo)currentUser.getPrincipal();

        //获取到当前用户部门id
        Long deptId = subject.getDeptId();

        List<SysDept> sysDeptInfos = sysDeptDao.selectDeptInfosByParentId(deptId);

        //判断是否为班组层级的id
        if(!ObjectUtils.isEmpty(sysDeptInfos)){

            List<SysDept> list = new ArrayList<>();

            for (SysDept sysDeptInfo : sysDeptInfos) {

                //获取班组层级id列表
                List<SysDept> sysDeptList = sysDeptDao.selectDeptInfosByParentId(sysDeptInfo.getId());

                list.addAll(sysDeptList);
            }

            if(!ObjectUtils.isEmpty(list)){

                List<SysDept> temp = new ArrayList<>();
                for (SysDept sysDeptTemp : list) {

                    //获取班组层级id列表
                    List<SysDept> sysDeptListTemp = sysDeptDao.selectDeptInfosByParentId(sysDeptTemp.getId());

                    temp.addAll(sysDeptListTemp);
                }

                List<WeldStatisticsData> weldStatisticsDataList = new ArrayList<>();
                if(!ObjectUtils.isEmpty(temp)){

                    List<Long> ids = new ArrayList<>();

                    for (SysDept sysInfo : temp) {
                        Long id = sysInfo.getId();
                        ids.add(id);
                    }
                    //执行班组生产数据报表查询
                    weldStatisticsDataList = teamDao.getList(time1,time2,ids);

                    return weldStatisticsDataList;
                }
                return weldStatisticsDataList;
            }

            //当前 sysDeptInfos 为装焊区层级id列表
            List<WeldStatisticsData> weldStatisticsDataList = new ArrayList<>();

            return weldStatisticsDataList;

        }

        //执行班组生产数据报表查询
        List<WeldStatisticsData> weldStatisticsDataList = new ArrayList<>();

        return weldStatisticsDataList;
    }
}
