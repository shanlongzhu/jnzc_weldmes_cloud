package com.gw.data.person.service.impl;

import com.gw.data.person.dao.PersonDao;
import com.gw.data.person.service.PersonService;
import com.gw.entities.RealtimeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonDao personDao;

    @Override
    public List<RealtimeData> getList(String time1, String time2) {
        return personDao.getList(time1,time2);
    }
}
