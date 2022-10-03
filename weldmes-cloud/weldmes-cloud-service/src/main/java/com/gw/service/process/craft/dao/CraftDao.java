package com.gw.service.process.craft.dao;

import com.gw.entities.WpsNorm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CraftDao {

    List<WpsNorm> getList(Long id);

    int addCraft(WpsNorm wpsNorm);

    List<WpsNorm> getById(Long id);

    int updateCraft(WpsNorm wpsNorm);

    int deleteCraft(Long id);

    /**
     * @Date 2021/7/2 16:08
     * @Description 根据 工艺库id  查询  通道号
     * @Params 工艺库id
     */
    List<Integer> queryChannelNos(@Param("id") Long id);
}
