package com.gw.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
public class MachineGatherInfo implements Serializable {

    /**
     * 主键
     */
    private long id;

    /**
     * 采集编号
     */
    private String gatherNo;

    /**
     * 采集盒状态
     */
    private int status;

    /**
     * 机构ID
     */
    private long deptId;

    /**
     * 生产日期
     */
    private String productionDate;

    /**
     * IP地址
     */
    private String ipPath;

    /**
     * mac地址
     */
    private String macPath;

    /**
     * 协议
     */
    private int protocol;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新人
     */
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    private String lastUpdateTime;

    /**
     * 字典类
     */
    private SysDictionary sysDictionary;

    /**
     * 部门类
     */
    private SysDept sysDept;

}
