package com.gw.entities;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
@ColumnWidth(12)
public class MachineGatherInfo {

    /**
     * 主键
     */
    @ExcelIgnore
    private long id;

    /**
     * 采集编号
     */
    @ExcelProperty(value = "采集编号",index = 0)
    private String gatherNo;

    /**
     * 所属项目
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "所属项目",index = 1)
    private String deptName;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态",index = 2)
    private String statusStr;

    /**
     * 协议
     */
    @ExcelProperty(value = "通讯协议",index = 3)
    private String protocolStr;

    /**
     * IP地址
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "IP地址",index = 4)
    private String ipPath;

    /**
     * mac地址
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "MAC地址",index = 5)
    private String macPath;

    /**
     * 生产日期
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "出厂时间",index = 6)
    private String productionDate;

    /**
     * 采集盒状态 字典
     */
    @ExcelIgnore
    private int status;

    /**
     * 机构ID
     */
    @ExcelIgnore
    private long deptId;

    /**
     * 协议 字典
     */
    @ExcelIgnore
    private int protocol;

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
    private String lastUpdateTime;

}
