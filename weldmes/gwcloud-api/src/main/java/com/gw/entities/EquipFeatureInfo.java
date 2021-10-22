package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/10/21 16:00
 * @Description 设备特征
 */
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
