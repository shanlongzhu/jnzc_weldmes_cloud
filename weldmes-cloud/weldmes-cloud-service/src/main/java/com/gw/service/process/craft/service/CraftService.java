package com.gw.service.process.craft.service;

import com.gw.entities.WpsLibrary;
import com.gw.entities.WpsNorm;

import java.util.List;

public interface CraftService {

    List<WpsNorm> getList(Long id);

    int addCraft(WpsNorm wpsNorm);

    List<WpsNorm> getById(Long id);

    int updateCraft(WpsNorm wpsNorm);

    int deleteCraft(Long id);

    /**
     * @Date 2021/7/2 16:08
     * @Description  根据 工艺库id  查询  通道号
     * @Params  工艺库id
     */
    List<Integer> getChannelNos(Long id);

}
