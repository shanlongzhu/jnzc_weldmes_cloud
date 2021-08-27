package com.gw.entities;

import lombok.Data;

import java.util.List;

/**
 * @Author zhanghan
 * @Date 2021/8/26 10:58
 * @Description 班组区域跨间信息类
 */
@Data
public class DeptAreaBayInfo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 作业区id(机构id)
     */
    private Long deptId;

    /**
     * 区域id
     */
    private Long areaId;

    /**
     * 跨间id
     */
    private List<Long> bayIds;

    /**
     * 跨间
     */
    private String bay;

    /**
     * 跨间
     */
    private Long bayId;

    /**
     * 创建时间
     */
    private String createTime;

}
