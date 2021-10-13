package com.gw.entities;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import java.io.Serializable;

/**
 * @Date 2021/10/12 17:18
 * @Description 焊工信息实体类
 * @Params
 */
@Data
@ColumnWidth(12)
public class WelderInfo implements Serializable {

    /**
     * 主键
     */
    @ExcelIgnore
    private Long id;

    /**
     * 焊工姓名
     */
    @ExcelProperty(value = "焊工姓名",index = 0)
    private String welderName;

    /**
     * 焊工编号（卡号）
     */
    @ExcelProperty(value = "焊工编号",index = 1)
    private String welderNo;

    /**
     * 焊工性别
     */
    @ExcelProperty(value = "焊工性别",index = 2)
    private String sex;

    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码",index = 3)
    private String cellphone;

    /**
     * 级别
     */
    @ExcelProperty(value = "级别",index = 4)
    private String rankStr;

    /**
     * 资质
     */
    @ExcelProperty(value = "资质",index = 5)
    private String certificationStr;

    /**
     * 部门
     */
    @ExcelProperty(value = "部门",index = 6)
    private String deptName;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注",index = 7)
    private String remarks;

    /**
     * 焊工状态，字典
     */
    @ExcelIgnore
    private Byte status;

    /**
     * 组织机构id
     */
    @ExcelIgnore
    private Long deptId;

    /**
     * 焊工性别 字典
     */
    @ExcelIgnore
    private Byte gender;

    /**
     * 级别，字典
     */
    @ExcelIgnore
    private Byte rank;

    /**
     * 资质，字典
     */
    @ExcelIgnore
    private Byte certification;

}
