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

        //如果传入时间有一个为空
        if (ObjectUtils.isEmpty(time1) || ObjectUtils.isEmpty(time1)){

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

                if(!ObjectUtils.isEmpty(temp)){

                    //执行班组生产数据报表查询
                    List<WeldStatisticsData> weldStatisticsDataList = getGradeInfo(time1,time2,temp);

                    return weldStatisticsDataList;
                }

                //用户部门id为制造部层级
                List<WeldStatisticsData> weldStatisticsDataList = getGradeInfo(time1,time2,list);

                return weldStatisticsDataList;
            }

            //当前 sysDeptInfos 为装焊区层级id列表
            List<WeldStatisticsData> weldStatisticsDataList = getGradeInfo(time1,time2,sysDeptInfos);

            return weldStatisticsDataList;

        }

        List<Long> ids = new ArrayList<>();

        ids.add(deptId);

        //执行班组生产数据报表查询  班组层级
        List<WeldStatisticsData> weldStatisticsDataList = teamDao.getList(time1,time2,ids);

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
