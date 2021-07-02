package com.gw.sys.service.impl;

import com.gw.common.MenuEnum;
import com.gw.entities.SysMenuInfo;
import com.gw.entities.UserOfSys;
import com.gw.sys.dao.UserRolesAndPerDao;
import com.gw.sys.service.UserRolesAndPerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @Author zhanghan
 * @Date 2021/6/4 13:19
 * @Description   用户角色权限业务层实现类
 */
@Service
public class UserRolesAndPerServiceImpl implements UserRolesAndPerService {

    @Autowired
    UserRolesAndPerDao userRolesAndPerDao;

    /**
     * @Date 2021/6/4 13:20
     * @Description  通过用户名 查询用户信息
     * @Params userName 用户名
     */
    @Override
    public UserOfSys queryUserInfoByUserNameAndPwd(String userName) {

        UserOfSys sysUser = userRolesAndPerDao.queryUserInfoByUserNameAndPwd(userName);

        return sysUser;
    }

    /**
     * @Date 2021/6/7 11:19
     * @Description  通过 用户角色id 查询 用户菜单id
     * @Params  id   用户角色id
     */
    @Override
    public List<Long> queryUserMenuIdList(Long id) {

        //获取用户菜单id
        List<Long> menuIds = userRolesAndPerDao.queryMenuIdByRoleId(id);

        return menuIds;
    }

    /**
     * @Date 2021/6/7 15:03
     * @Description  通过用户菜单目录id 角色id  获取菜单栏目录信息  子菜单信息 以及 子菜单所拥有的功能按钮信息
     * @Params menuIds 菜单目录id列表  roleIds 角色id列表
     */
    @Override
    public List<SysMenuInfo> queryMenuInfo(List<Long> menuIds,List<Long> roleIds) {

        //最终返回给前端 该用户 所有角色 的目录信息
        List<SysMenuInfo> menuInfoList = new ArrayList<>();

        //遍历该用户拥有的每一个目录
        for (Long menuId : menuIds) {

            //通过 菜单id 获取用户 当前菜单目录
            SysMenuInfo menuInfo = userRolesAndPerDao.queryMenuInfoByMenuId(menuId);

            //通过当前菜单目录id 角色id 获取 到用户在当前 目录中 所拥有角色的 子菜单id
            List<Long> childrenMenuIdList = getMenuIds(roleIds,menuId);

            //子菜单id列表 去重  防止多角色子菜单信息重复
            List<Long> childrenMenuIdListTemp = removeDuplicate(childrenMenuIdList);

            List<SysMenuInfo> childrenMenuList = new ArrayList<>();

            for (Long childrenMenuId : childrenMenuIdListTemp) {

                //获取到当前目录 当前子菜单信息
                SysMenuInfo childrenMenuInfo = userRolesAndPerDao.queryMenuInfoByMenuId(childrenMenuId);

                //通过子菜单id 角色id 获取到 用户在当前目录中当前子菜单中 所拥有角色 的按钮信息id或二级菜单id
                List<Long> buttonOrSecondMenuIdList = getMenuIds(roleIds,childrenMenuId);

                //二级菜单id/按钮菜单id列表 去重
                List<Long> buttonOrSecondMenuIdListTemp = removeDuplicate(buttonOrSecondMenuIdList);

                if(buttonOrSecondMenuIdListTemp.size() > 0){
                    //抽取一条信息进行验证
                    SysMenuInfo info = userRolesAndPerDao.queryMenuInfoByMenuId(buttonOrSecondMenuIdListTemp.get(0));

                    //判断是否是二级菜单id
                    if(info.getType() == MenuEnum.SECOND_MENU_FLAG.getId()){

                        List<SysMenuInfo> secondMenuInfoCollection = new ArrayList<>();

                        for (Long secondMenuId : buttonOrSecondMenuIdListTemp) {

                            //获取当前二级菜单信息
                            SysMenuInfo secondMenuInfo = userRolesAndPerDao.queryMenuInfoByMenuId(secondMenuId);

                            //获取到用户 当前目录 当前子菜单 当前二级菜单中 所拥有角色 的 按钮id
                            List<Long> buttonIdCollection = getMenuIds(roleIds,secondMenuId);

                            //获取 当前二级菜单中的所有按钮信息
                            List<SysMenuInfo> buttonInfoCollection = getButtonInfo(buttonIdCollection);

                            //将按钮信息封装进当前二级菜单中
                            secondMenuInfo.setMenuButtonInfos(buttonInfoCollection);

                            //二级菜单汇总
                            secondMenuInfoCollection.add(secondMenuInfo);

                        }

                        //将二级菜单信息放入当前子菜单
                        childrenMenuInfo.setChildrenMenuInfos(secondMenuInfoCollection);

                        //将子菜单汇总
                        childrenMenuList.add(childrenMenuInfo);
                    }else{

                        //获取当前子菜单所有按钮信息
                        List<SysMenuInfo> buttonInfos = getButtonInfo(buttonOrSecondMenuIdListTemp);

                        //将按钮信息封装进当前子菜单中
                        childrenMenuInfo.setMenuButtonInfos(buttonInfos);

                        //将子菜单汇总
                        childrenMenuList.add(childrenMenuInfo);
                    }
                }
                //将子菜单汇总后 放入当前目录
                menuInfo.setChildrenMenuInfos(childrenMenuList);
            }
            //进行目录汇总
            menuInfoList.add(menuInfo);
        }
        return menuInfoList;
    }

    /**
     * @Date 2021/6/7 16:55
     * @Description  通过用户id查询 用户角色id
     * @Params
     */
    @Override
    public List<Long> queryUserRoleIdList(Long id) {

        List<Long> roleIds = userRolesAndPerDao.queryUserRoleIds(id);

        return roleIds;
    }

    /**
     * @Date 2021/6/7 17:59
     * @Description 通过用户角色id 获取用户角色名称列表
     * @Params
     */
    @Override
    public List<String> queryUserRoles(List<Long> ids) {

        List<String> list = new ArrayList<>();

        for (Long id:ids) {

            String roleName = userRolesAndPerDao.queryUserRoleName(id);

            list.add(roleName);
        }

        return list;
    }

    /**
     * @Date 2021/6/8 18:40
     * @Description list去重
     * @Params
     */
    public List<Long> removeDuplicate(List<Long> list){

        // set集合特性会自动去重复
        Set set = new HashSet();

        // 将list所有元素添加到set中
        set.addAll(list);

        list.clear();

        // 将list清空并将set中的所有元素添加到list中
        list.addAll(set);

        return list;
    }

    /**
     * @Date 2021/6/9 16:35
     * @Description  获取按钮信息
     * @Params buttonIds 按钮id列表
     */
    public List<SysMenuInfo> getButtonInfo(List<Long> buttonIds){

        List<SysMenuInfo> list = new ArrayList<>();

        for (Long buttonId : buttonIds) {

            SysMenuInfo buttonInfo = userRolesAndPerDao.queryMenuInfoByMenuId(buttonId);

            list.add(buttonInfo);

        }
        return list;
    }

    /**
     * @Date 2021/6/9 16:53
     * @Description 通过菜单目录id/子菜单id/二级菜单id、角色id  查询  子菜单id/按钮id或二级菜单id/按钮id
     * @Params
     */
    public List<Long> getMenuIds(List<Long> roleIds,Long menuId){

        List<Long> list = new ArrayList<>();

        for (Long roleId : roleIds) {

            List<Long> buttonOrSecondMenuInfoMenuIds = userRolesAndPerDao.queryChildrenMenuIdBySuperMenuIdAndRoleId(menuId,roleId);

            list.addAll(buttonOrSecondMenuInfoMenuIds);
        }
        return list;
    }
}
