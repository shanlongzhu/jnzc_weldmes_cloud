package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/7/15 16:47
 * @Description 厂家设备实体类
 */
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
