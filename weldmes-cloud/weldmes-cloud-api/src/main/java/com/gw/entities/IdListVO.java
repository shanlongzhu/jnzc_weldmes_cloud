package com.gw.entities;

import lombok.Data;

import java.util.List;

@Data
public class IdListVO {

    /**
     * id列表
     */
    List<Long> idList;

    /**
     * 区域id列表
     */
    List<AreaBayInfo> areaBayInfos;

    /**
     * 厂商id列表
     */
    List<FirmMachineInfo> firmMachineInfos;
}
