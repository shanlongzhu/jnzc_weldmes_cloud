package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/7/15 16:23
 * @Description 区域跨间实体类
 */
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
}