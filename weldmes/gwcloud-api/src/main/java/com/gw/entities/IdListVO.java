package com.gw.entities;

import lombok.Data;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/6/1 11:24
 * @Description  批量删除id列表实体类   用于Controller接收 传参
 */
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
