package com.shth.das;

import com.shth.das.pojo.db.GatherModel;
import com.shth.das.sys.weldmesdb.mapper.MachineGatherMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Test
    void testTime(){
        String str = "210408123011";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        System.out.println(dateTime.toString().replace("T"," "));
    }


}
