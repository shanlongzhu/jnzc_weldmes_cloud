package com.gw.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.CommonUtil;
import com.gw.common.ConstantInfo;
import com.gw.common.DateTimeUtil;
import com.gw.common.PageInfo;
import com.gw.entities.MenuAndButtonInfo;
import com.gw.entities.SysMenu;
import com.gw.sys.dao.SysMenuDao;
import com.gw.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    SysMenuDao sysMenuDao;

    @Override
    public PageInfo<SysMenu> getSysMenuPage(int draw, int start, int length, SysMenu sysMenu) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.like(CommonUtil.isNotEmpty(sysMenu.getName()), "name", sysMenu.getName());
        start = (start / length) + 1;//当前页码
        IPage<SysMenu> page = new Page<>(start, length);
        IPage<SysMenu> sysDictionaryIPage = sysMenuDao.selectPage(page, wrapper);
        PageInfo<SysMenu> pageInfo = new PageInfo<>();
        pageInfo.setDraw(draw);
        pageInfo.setData(sysDictionaryIPage.getRecords());//数据结果
        pageInfo.setRecordsTotal(sysDictionaryIPage.getTotal());//总数
        pageInfo.setRecordsFiltered(sysDictionaryIPage.getRecords().size());////过滤后的总记录数
        return pageInfo;
    }

    @Override
    public int addSysMenu(SysMenu sysMenu) {
        return sysMenuDao.insert(sysMenu);
    }

    @Override
    public int updateSysMenu(SysMenu sysMenu) {
        return sysMenuDao.updateById(sysMenu);
    }

    @Override
    public int deleteSysMenu(List<BigInteger> ids) {
        return sysMenuDao.deleteBatchIds(ids);
    }

    /**
     * @Date 2021/7/2 15:56
     * @Description  新增 目录/菜单/按钮信息
     * @Params menuAndButtonInfo 目录/菜单/按钮信息
     */
    @Override
    public void addMuenOrButtonInfo(MenuAndButtonInfo menuAndButtonInfo) {

        //获取当前时间
        String createTime = DateTimeUtil.getCurrentTime();

        menuAndButtonInfo.setCreateTime(createTime);

        sysMenuDao.insertMenuAndButtonInfo(menuAndButtonInfo);
    }


    /**
     * @Date 2021/7/5 9:49
     * @Description 拉取菜单列表信息
     * @Params
     */
    @Override
    public List<MenuAndButtonInfo> getMenuInfoList() {

        //获取顶级菜单
        List<MenuAndButtonInfo> list = sysMenuDao.selectMenuInfoList();

        //获取菜单信息列表中的每一个顶级菜单
        for (MenuAndButtonInfo menuAndButtonInfo : list) {

            //通过 顶级菜单的id 查询顶级菜单下的子菜单信息
            List<MenuAndButtonInfo> childrenMenuInfos = sysMenuDao.selectChildrenMenuInfoListByMenuId(menuAndButtonInfo.getId());

            //获取每一个顶级菜单中的每一个子菜单
            for (MenuAndButtonInfo childrenMenuInfo : childrenMenuInfos) {

                //通过 子菜单的id 查询按钮信息 或 三级菜单信息
                List<MenuAndButtonInfo> buttonOrThreeLevelMenuInfos = sysMenuDao.selectChildrenMenuInfoListByMenuId(childrenMenuInfo.getId());

                for (MenuAndButtonInfo buttonOrThreeLevelMenuInfo : buttonOrThreeLevelMenuInfos) {

                    //判断是否存在三级菜单
                    if(buttonOrThreeLevelMenuInfo.getType().equals(ConstantInfo.BUTTON_FLAG)){

                        //不存在三级菜单时  直接将按钮信息放入子菜单中
                        childrenMenuInfo.setButtons(buttonOrThreeLevelMenuInfos);
                        break;
                    }

                    List<MenuAndButtonInfo> threeLevelMenuButtonInfos = sysMenuDao.selectChildrenMenuInfoListByMenuId(buttonOrThreeLevelMenuInfo.getId());

                    //存在三级菜单时   将按钮信息放入三级菜单中
                    buttonOrThreeLevelMenuInfo.setButtons(threeLevelMenuButtonInfos);

                }
                //将三级菜单信息放入子菜单中
                childrenMenuInfo.setThreeLevelMenuInfos(buttonOrThreeLevelMenuInfos);
            }

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
}
