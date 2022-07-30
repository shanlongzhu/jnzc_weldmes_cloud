package com.gw.entities;

import lombok.Data;


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
