package com.gw.process.craft.service;

import com.gw.entities.WpsLibrary;
import com.gw.entities.WpsNorm;

import java.util.List;

public interface CraftService {

    List<WpsNorm> getList(Long id);

    int addCraft(WpsNorm wpsNorm);

    List<WpsNorm> getById(Long id);

    int updateCraft(WpsNorm wpsNorm);

    int deleteCraft(Long id);
}
