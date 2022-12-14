package com.gw.service.sys.service.impl;

import com.gw.common.DateTimeUtils;
import com.gw.entities.SysMenuInfo;
import com.gw.entities.UserLoginInfo;
import com.gw.service.sys.dao.SysMenuDao;
import com.gw.service.sys.dao.UserRolesAndPerDao;
import com.gw.service.sys.service.SysMenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    SysMenuDao sysMenuDao;

    @Autowired
    UserRolesAndPerDao userRolesAndPerDao;

    /**
     * @Date 2021/7/2 15:56
     * @Description  新增 目录/菜单/按钮信息
     * @Params menuAndButtonInfo 目录/菜单/按钮信息
     */
    @Override
    public void addMuenOrButtonInfo(SysMenuInfo menuAndButtonInfo) {

        //获取当前时间
        String createTime = DateTimeUtils.getNowDateTime();

        menuAndButtonInfo.setCreateTime(createTime);

        sysMenuDao.insertMenuAndButtonInfo(menuAndButtonInfo);
    }


    /**
     * @Date 2021/7/5 9:49
     * @Description 拉取菜单列表信息
     * @Params
     */
    @Override
    public List<SysMenuInfo> getMenuInfoList() {

        //获取顶级菜单
        List<SysMenuInfo> list = sysMenuDao.selectMenuInfoList();

        //获取菜单信息列表中的每一个顶级菜单
        for (SysMenuInfo menuAndButtonInfo : list) {

            //递归查询顶级菜单下的子菜单信息
            List<SysMenuInfo> childrenMenuInfos = getMenuInfoList(menuAndButtonInfo);

            //将子菜单信息放入顶级菜单中
            menuAndButtonInfo.setMenus(childrenMenuInfos);
        }

        return list;
    }

    /**
     * @Date 2021/7/5 15:33
     * @Description  根据id 删除菜单/按钮
     * @Params id 菜单/按钮id
     */
    @Override
    public void delMenuOrButoonInfoById(Long id) {

        sysMenuDao.deleteMenuOrButtonInfoById(id);

    }

    /**
     * @Date 2021/7/5 16:34
     * @Description  根据当前用户角色 查询用户的菜单以及按钮权限
     * @Params
     */
    @Override
    public Map<String,Object> getCurrentUserMenuAndButtonInfos() {

        Subject currentUser = SecurityUtils.getSubject();

        //获取当前用户信息
        UserLoginInfo userInfo = (UserLoginInfo)currentUser.getPrincipal();

        //获取当前用户角色id
        List<Long> rolesIds = userRolesAndPerDao.queryUserRoleIds(userInfo.getId());

        Map<String,Object> map = new HashMap<>();

        if(ObjectUtils.isEmpty(rolesIds)){

            List<SysMenuInfo> menusTemp = new ArrayList<>();
            map.put("menus",menusTemp);

            return map;
        }
        //获取菜单/按钮id
        for (Long rolesId : rolesIds) {

            //获取 菜单Id列表
            List<Long> menuIds = userRolesAndPerDao.queryMenuIdByRoleId(rolesId);

            if(!ObjectUtils.isEmpty(menuIds)){

                //获取菜单信息列表
                List<SysMenuInfo> menus = sysMenuDao.queryMenuInfoByMenuId(menuIds);

                map.put("menus",menus);
            }

        }

        return map;
    }

    /**
     * @Date 2021/7/5 15:33
     * @Description  修改菜单/按钮权限信息
     * @Params  menuAndButtonInfo 菜单/按钮权限信息
     */
    @Override
    public void updateMenuOrButtonInfo(SysMenuInfo menuAndButtonInfo) {

        //获取系统当前时间
        String time = DateTimeUtils.getNowDateTime();

        menuAndButtonInfo.setLastUpdateTime(time);

        sysMenuDao.updateMenuOrButtonInfo(menuAndButtonInfo);

    }

    /**
     * @Date 2021/7/6 10:49
     * @Description  根据id查询菜单/按钮信息
     * @Params id  菜单/按钮id
     */
    @Override
    public SysMenuInfo getMenuOrButtonInfoById(Long id) {

        //用id列表查询菜单信息  所以通过  list包装
        List<Long> list = new ArrayList<>();

        SysMenuInfo sysMenuInfo = new SysMenuInfo();

        list.add(id);

        List<SysMenuInfo> menus = sysMenuDao.queryMenuInfoByMenuId(list);

        if(ObjectUtils.isEmpty(menus)){

            return sysMenuInfo;
        }

        //获取到目标菜单/按钮信息
        sysMenuInfo = menus.get(0);

        list.clear();

        list.add(sysMenuInfo.getParentId());

        List<SysMenuInfo> menuList = sysMenuDao.queryMenuInfoByMenuId(list);

        if(!ObjectUtils.isEmpty(menuList)){

            //获取目标菜单/按钮信息 的 父级菜单/按钮信息
            SysMenuInfo sysParentMenuInfo = menuList.get(0);

            //将父级菜单/按钮名称  放到  目标菜单/按钮中
            sysMenuInfo.setParentName(sysParentMenuInfo.getLabel());
        }

        return sysMenuInfo;
    }

    /**
     * @Date 2021/7/6 10:49
     * @Description  根据角色  查询该角色的菜单以及按钮权限
     * @Params  id  角色id
     */
    @Override
    public List<SysMenuInfo> getMenuOrButtonInfoByRole(Long id) {

        //获取 菜单Id列表
        List<Long> menuIds = userRolesAndPerDao.queryMenuIdByRoleId(id);

        List<SysMenuInfo> menus = new ArrayList<>();

        if(ObjectUtils.isEmpty(menuIds)){
            return menus;
        }
        //获取菜单信息列表
        menus = sysMenuDao.queryMenuInfoByMenuId(menuIds);

        return menus;
    }

    /**
     * @Date 2021/7/31 13:52
     * @Description  递归查询菜单树状信息
     * @Params menuAndButtonInfo 顶级菜单项信息
     */
    private List<SysMenuInfo> getMenuInfoList(SysMenuInfo menuAndButtonInfo) {

        //通过 顶级菜单的id 查询顶级菜单下的子菜单信息
        List<SysMenuInfo> childrenMenuInfos = sysMenuDao.selectChildrenMenuInfoListByMenuId(menuAndButtonInfo.getId());

        if (!ObjectUtils.isEmpty(childrenMenuInfos)){

            for (SysMenuInfo sysMenuInfoInfoTemp : childrenMenuInfos) {

                List<SysMenuInfo> nextSysDeptInfos =getMenuInfoList(sysMenuInfoInfoTemp);

                sysMenuInfoInfoTemp.setMenus(nextSysDeptInfos);

            }
        }
        return childrenMenuInfos;
    }
}
