package com.shth.das.pojo.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 采集模块实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName(value = "machine_gather_info")
public class GatherModel implements Serializable {

    @TableId(type = IdType.AUTO)
    private BigInteger id;
    @TableField("gather_no")
    private String gatherNo;
    private Integer status;
    @TableField("dept_id")
    private BigInteger deptId;
    @TableField("production_date")
    private String productionDate;
    @TableField("ip_path")
    private String ipPath;
    @TableField("mac_path")
    private String macPath;
    private String protocol;

}
