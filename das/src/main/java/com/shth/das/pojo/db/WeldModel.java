package com.shth.das.pojo.db;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 焊机实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
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
