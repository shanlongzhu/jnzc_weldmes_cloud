package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/8/12 15:13
 * @Description 焊机厂家和采集编号实体类
 */
@Data
public class GatherAndFirmInfo {

    /**
     * 厂家code   0 - OTC    1 - 松下
     */
    private String firmCode;

    /**
     * 采集编号
     */
    private String gatherNo;
}
