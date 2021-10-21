package com.gw.data.person.service.impl;

import com.gw.data.person.dao.PersonDao;
import com.gw.data.person.service.PersonService;
import com.gw.entities.WeldStatisticsDataPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonDao personDao;

    /**
     * @Date 2021/10/18 16:23
     * @Description  获取人员生产数据列表
     * @Params
     */
    @Override
    public List<WeldStatisticsDataPerson> getList(String time1, String time2,
                                                  String welderNo, String welderName, List<Long> ids) {

        List<WeldStatisticsDataPerson> list = personDao.getList(time1,time2,welderNo,welderName,ids);

        return list;
    }
}
