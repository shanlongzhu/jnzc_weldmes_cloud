package com.gw.process.craft.dao;

import com.gw.entities.WpsLibrary;
import com.gw.entities.WpsNorm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CraftDao {

    List<WpsNorm> getList(Long id);

    int addCraft(WpsNorm wpsNorm);

    List<WpsNorm> getById(Long id);

    int updateCraft(WpsNorm wpsNorm);

    int deleteCraft(Long id);
}
