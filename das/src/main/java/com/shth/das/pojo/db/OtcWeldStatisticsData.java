package com.shth.das.pojo.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@TableName(value = "weld_statistics_data")
public class OtcWeldStatisticsData {

    @TableId(type = IdType.AUTO)
    private BigInteger id;
    private Integer weldModel;
    private String gatherNo;
    private String welderNo;
    private Integer weldStatus;
    private BigInteger weldDuration;
    private BigInteger machineId;
    private String machineNo;
    private BigInteger machineDeptId;
    private BigInteger gatherId;
    private BigInteger gatherDeptId;
    private BigInteger welderId;
    private String welderName;
    private BigInteger welderDeptId;
    private BigInteger taskId;
    private String taskName;
    private String taskNo;
    private Date createTime;
    private Date startTime;
    private Date endTime;
    private BigDecimal electricity;
    private BigDecimal voltage;
    private BigDecimal wireFeedRate;
    private BigDecimal wireDiameter;
    private BigDecimal wireMaterialsGases;

}
