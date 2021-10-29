package com.gw.config;

import com.gw.common.ConstantInfo;
import com.gw.common.TableStrategy;

/**
 * @Author zhanghan
 * @Date 2021/7/20 14:22
 * @Description
 */
public class Test {

    public static void main(String[] args) {

        String time2 = "2021-10-28 23:59:59";
        //截取time2的日期与时间
        String date = time2.split(" ")[0];

        String nowTime = time2.split(" ")[1];

        System.out.println(date.split("-")[0]+date.split("-")[1]+date.split("-")[2]);

        String tableOTCName = ConstantInfo.OTC_TABLE_NAME_PREFIX_FLAG + date.split("-")[0]+date.split("-")[1]+date.split("-")[2];

        System.out.println(nowTime.split(":")[0]+":00:00");


    }
}
