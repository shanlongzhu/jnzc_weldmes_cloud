package com.gw.entities;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ColumnWidth(12)
@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
public class MachineWeldInfo implements Serializable {

    /**
     * 主键
     */
    @ExcelIgnore
    private long id;

    /**
     * 设备编号/固定资产编号
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "固定资产编号",index = 0)
    private String machineNo;

    /**
     * 设备类型
     */
    @ExcelProperty(value = "设备类型",index = 1)
    private String typeStr;

    /**
     * 生产日期
     */
    @ExcelProperty(value = "入厂时间",index = 2)
    private Timestamp productionDate;

    /**
     * 所属项目
     */
    @ExcelProperty(value = "所属项目",index = 3)
    private String deptName;

    /**
     * 设备状态
     */
    @ExcelProperty(value = "状态",index = 4)
    private String statusStr;

    /**
     * 厂家
     */
    @ExcelProperty(value = "厂家",index = 5)
    private String firmStr;

    /**
     * 是否联网（0：是 1：否）
     */
    @ExcelProperty(value = "是否在网",index = 6,converter = IsNetworkConverter.class)
    private Long isNetwork;

    /**
     * 采集编号
     */
    @ExcelProperty(value = "采集编号",index = 7)
    private String gatherNo;

    /**
     * 区域
     */
    @ExcelProperty(value = "区域",index = 8)
    private String areaStr;

    /**
     * 跨间
     */
    @ExcelProperty(value = "跨间",index = 9)
    private String bayStr;

    /**
     * ip地址
     */
    @ExcelProperty(value = "IP地址",index = 10)
    private String ipPath;

    /**
     * 设备型号
     */
    @ExcelProperty(value = "设备型号",index = 11)
    private String modelStr;

    /**
     * 设备状态 字典
     */
    @ExcelIgnore
    private Byte status;

    /**
     * 类型 字典
     */
    @ExcelIgnore
    private Byte type;

    /**
     * 厂商 字典
     */
    @ExcelIgnore
    private Byte firm;

    /**
     * 型号 字典
     */
    @ExcelIgnore
    private Byte model;

    /**
     * 部门机构 字典
     */
    @ExcelIgnore
    private long deptId;

    /**
     * 采集id
     */
    @ExcelIgnore
    private Long gId;

    /**
     * 创建人
     */
    @ExcelIgnore
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelIgnore
    private String createTime;

    /**
     * 更新人
     */
    @ExcelIgnore
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    @ExcelIgnore
    private Timestamp lastUpdateTime;

    /**
     * 区域(字典)
     */
    @ExcelIgnore
    private Long area;

    /**
     * 跨间(字典)
     */
    @ExcelIgnore
    private Long bay;

    /**
     * 绑定任务标识
     */
    @ExcelIgnore
    private int taskFlag;

    /**
     * 设备标识
     */
    @ExcelIgnore
    private String macFlag;


}
