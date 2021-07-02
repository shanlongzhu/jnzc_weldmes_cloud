package com.gw.process.craft.service.impl;

import com.gw.entities.WpsLibrary;
import com.gw.entities.WpsNorm;
import com.gw.process.craft.dao.CraftDao;
import com.gw.process.craft.service.CraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CraftServiceImpl implements CraftService {

    @Autowired
    private CraftDao craftDao;

    @Override
    public List<WpsNorm> getList(Long id) {
        return craftDao.getList(id);
    }

    @Override
    public int addCraft(WpsNorm wpsNorm) {
       return craftDao.addCraft(wpsNorm);
    }

    @Override
    public List<WpsNorm> getById(Long id) {
        return craftDao.getById(id);
    }

    @Override
    public int updateCraft(WpsNorm wpsNorm) {
        return craftDao.updateCraft(wpsNorm);
    }

    @Override
    public int deleteCraft(Long id) {
        return craftDao.deleteCraft(id);
    }

    /**
     * @Date 2021/7/2 16:08
     * @Description  根据 工艺库id  查询  通道号
     * @Params  工艺库id
     */
    @Override
    public List<Integer> getChannelNos(Long id) {

        List<Integer> list = craftDao.queryChannelNos(id);

        return list;
    }
}
