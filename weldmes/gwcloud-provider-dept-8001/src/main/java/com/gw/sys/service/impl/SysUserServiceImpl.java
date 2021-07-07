package com.gw.sys.service.impl;


import com.gw.entities.SysDeptInfo;
import com.gw.process.dispatch.dao.DispatchDao;
import com.gw.sys.dao.UserRolesAndPerDao;
import com.gw.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/4 10:12
 * @Description  用户业务实现层
 * @Params
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    UserRolesAndPerDao userRolesAndPerDao;

    @Autowired
    DispatchDao dispatchDao;

    /**
     * @Date 2021/7/7 10:40
     * @Description  获取集团班组树状图信息
     * @Params
     */
    @Override
    public SysDeptInfo getGradeInfo() {

        /*Subject currentUser = SecurityUtils.getSubject();

        //获取到当前用户
        UserLoginInfo userLoginInfo = (UserLoginInfo)currentUser.getPrincipal();*/

        //根据用户部门id 获取到该用户所属部门的下属部门信息
        //List<DeptTreeInfo> deptTreeInfos = userRolesAndPerDao.selectGradeInfo(1L);

        //获取到当前用户所属的机构信息
        SysDeptInfo sysDeptInfo = dispatchDao.queryDeptNameListById(1L);

        //获取二级部门信息列表
        List<SysDeptInfo> sysSecondDeptInfos = dispatchDao.queryGradeList(sysDeptInfo.getId());

        if(!ObjectUtils.isEmpty(sysSecondDeptInfos)){

            for (SysDeptInfo sysSecondDeptInfo : sysSecondDeptInfos) {

                //获取三级部门信息列表
                List<SysDeptInfo> sysThirdDeptInfos = dispatchDao.queryGradeList(sysSecondDeptInfo.getId());

                if(!ObjectUtils.isEmpty(sysThirdDeptInfos)){

                    for (SysDeptInfo sysThirdDeptInfo : sysThirdDeptInfos) {

                        //获取四级部门信息列表
                        List<SysDeptInfo> sysFourthDeptInfos = dispatchDao.queryGradeList(sysThirdDeptInfo.getId());

                        if(!ObjectUtils.isEmpty(sysFourthDeptInfos)){

                            //将四级部门信息放入三级部门信息中
                            sysThirdDeptInfo.setList(sysFourthDeptInfos);
                        }
                    }
                    sysSecondDeptInfo.setList(sysThirdDeptInfos);
                }
            }
            sysDeptInfo.setList(sysSecondDeptInfos);
        }

        return sysDeptInfo;
    }
}
