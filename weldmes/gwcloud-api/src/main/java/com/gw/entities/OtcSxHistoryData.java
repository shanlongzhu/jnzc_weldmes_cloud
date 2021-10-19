package com.gw.entities;

import lombok.Data;

import java.io.Serializable;

/**
 * OTC历史数据实体类（查询ProcessDB）
 */
@Data
public class OtcSxHistoryData implements Serializable {

    /**
     * 电流
     */
    private Float electricity;
    /**
     * 电压
     */
    private Float voltage;
    /**
     * 时间
     */
    private String weldTime;
}
