package com.gw.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.CommonUtil;
import com.gw.common.PageInfo;
import com.gw.entities.SysUser;
import com.gw.sys.dao.SysUserDao;
import com.gw.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserDao sysUserDao;

    @Override
    public PageInfo<SysUser> UserPage(int draw, int start, int length, SysUser sysUser) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.like(CommonUtil.isNotEmpty(sysUser.getName()),"name",sysUser.getName());
        start = (start / length) + 1;//当前页码
        IPage<SysUser> page = new Page<>(start, length);
        IPage<SysUser> sysUserIPage = sysUserDao.selectPage(page, wrapper);
        PageInfo<SysUser> pageInfo = new PageInfo<>();
        pageInfo.setDraw(draw);
        pageInfo.setData(sysUserIPage.getRecords());//数据结果
        pageInfo.setRecordsTotal(sysUserIPage.getTotal());//总数
        pageInfo.setRecordsFiltered(sysUserIPage.getRecords().size());////过滤后的总记录数
        return pageInfo;
    }

    @Override
    public int addUser(SysUser sysUser) {
        return sysUserDao.insert(sysUser);
    }

    @Override
    public SysUser getUserById(SysUser sysUser) {
        return sysUserDao.selectById(sysUser.getId());
    }

    @Override
    public int updateUser(SysUser sysUser) {
        return sysUserDao.updateById(sysUser);
    }

    @Override
    public int deleteUser(List<BigInteger> ids) {
        return sysUserDao.deleteBatchIds(ids);
    }

    @Override
    public boolean login(SysUser sysUser) {
        try {
            if (CommonUtil.isNotEmpty(sysUser.getName()) && CommonUtil.isNotEmpty(sysUser.getPassword())) {
                QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name", sysUser.getName());
                queryWrapper.and(wrapper -> wrapper.eq("password", sysUser.getPassword()));
                int countUser = sysUserDao.selectCount(queryWrapper);
                return countUser > 0;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
