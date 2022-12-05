package com.shth.das.pojo.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 焊机实体类
 */
@Data
@TableName(value = "machine_weld_info")
public class WeldModel implements Serializable {

    @TableId(type = IdType.AUTO)
    private BigInteger id;
    @TableField("machine_no")
    private String machineNo;
    private Integer status;
    private Integer type;
    private Integer firm;
    private Integer model;
    @TableField("dept_id")
    private BigInteger deptId;
    @TableField("production_date")
    private String productionDate;

    @TableField(exist = false)  //数据库表不存在该字段
    private String gatherNo;//采集编号

}
