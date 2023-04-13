package com.shth.das.pojo.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@TableName(value = "weldsx_statistics_data")
public class SxWeldStatisticsData {

    @TableId(type = IdType.AUTO)
    private BigInteger id;
    private String weldCid;
    private String weldCode;
    private String weldIp;
    private String weldModel;
    private Integer weldType;
    private Integer weldStatus;
    private BigInteger weldSuration;
    private Date startTime;
    private Date endTime;
    private Date createTime;
    private BigDecimal initialEle;
    private BigDecimal initialVol;
    private BigDecimal initialWireSpeed;
    private BigDecimal weldEle;
    private BigDecimal weldVol;
    private BigDecimal weldWireSpeed;
    private BigDecimal arcEle;
    private BigDecimal arcVol;
    private BigDecimal arcWireSpeed;
    private BigDecimal realityWeldEle;
    private BigDecimal realityWeldVol;
    private BigDecimal realityWireSpeed;
    private Integer weldFlag;
    private BigInteger welderId;
    private String welderName;
    private BigInteger welderDeptId;
    private BigInteger taskId;
    private String taskName;
    private String taskNo;
    private BigDecimal wireSpeed;

}
