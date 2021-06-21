package com.shth.das.pojo;

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
 * 设备开关机时间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName(value = "weld_on_off_time")
public class WeldOnOffTime implements Serializable {

    @TableId(type = IdType.AUTO)
    private BigInteger id;      //ID
    @TableField("gather_no")
    private String gatherNo;    //采集编号
    @TableField("start_time")
    private String startTime;   //开机时间
    @TableField("end_time")
    private String endTime;     //关机时间
}
