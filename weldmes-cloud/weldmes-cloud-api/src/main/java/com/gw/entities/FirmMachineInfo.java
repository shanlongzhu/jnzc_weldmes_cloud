package com.gw.entities;

import lombok.Data;

@Data
public class FirmMachineInfo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 厂家id(字典)
     */
    private Long firmId;

    /**
     * 设备id(字典)
     */
    private Long machineId;

    /**
     * 备注
     */
    private String remarks;
}
