package com.gw.entities;

import lombok.Data;


@Data
public class EquipFeatureInfo {

    /**
     * 开机时间
     */
    private String StartUpTime;

    /**
     * 关机时间
     */
    private String ShutDownTime;

    /**
     * 工作时长
     */
    private String WorkHours;

    /**
     * 焊接时长
     */
    private String WeldingDuration;

    /**
     * 松下设备CID
     */
    private String weldCid;
}
