package com.shth.das;

import com.shth.das.pojo.GatherModel;
import com.shth.das.sys.weldmesdb.mapper.MachineGatherMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class DasApplicationTests {

    @Autowired
    MachineGatherMapper gatherMapper;

    @Test
    void contextLoads() {


        List<GatherModel> gatherModels = gatherMapper.selectList(null);

        System.out.println(gatherModels);

    }


}
