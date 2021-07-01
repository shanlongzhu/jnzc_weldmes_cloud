package com.gw.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
@TableName(value = "sys_log")//指定表名
public class SysLog implements Serializable {
    @TableId(type = IdType.AUTO)        //主见注解，数据库ID自增
    private BigInteger id;
    @TableField(value = "user_name")
    private String userName;
    private String operation;
    private String method;
    private String params;
    private BigInteger time;
    private String ip;
    @TableField(value = "create_by")
    private String createBy;
    @TableField("create_time")
    private String createTime;
    @TableField("last_update_by")
    private String lastUpdateBy;
    @TableField("last_update_time")
    private String lastUpdateTime;

    @TableField(exist = false)
    private String starttime;
    @TableField(exist = false)
    private String endtime;

}
