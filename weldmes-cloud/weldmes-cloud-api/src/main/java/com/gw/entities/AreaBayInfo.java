package com.gw.entities;

import lombok.Data;

import java.util.List;

@Data
public class AreaBayInfo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 区域字典主键
     */
    private Long areaId;

    /**
     * 跨间字典主键
     */
    private Long bayId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 区域跨间名称
     */
    private String areaBayName;

    /**
     * 列表
     */
    private List<AreaBayInfo> list;
}
