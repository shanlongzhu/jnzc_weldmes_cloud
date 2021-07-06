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
    /**
     *  ID
     */
    @TableId(type = IdType.AUTO)
    private BigInteger id;
    /**采集编号*/
    @TableField("gather_no")
    private String gatherNo;
    /**开机时间*/
    @TableField("start_time")
    private String startTime;
    /**关机时间*/
    @TableField("end_time")
    private String endTime;
    /**设备id(根据类型来区分)*/
    @TableField("machine_id")
    private BigInteger machineId;
    /**设备类型(默认0:OTC,1:松下)*/
    @TableField("machine_type")
    private int machineType;
    /**设备IP地址(只存松下)*/
    @TableField("weldsx_ip")
    private String weldsxIp;
}
