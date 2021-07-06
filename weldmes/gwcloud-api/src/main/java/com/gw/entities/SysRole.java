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


@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
@TableName(value = "sys_role")//指定表名
public class SysRole implements Serializable {
    @TableId(type = IdType.AUTO)        //主见注解，数据库ID自增
    private BigInteger id;
    private String name;
    private String remark;
    @TableField(value = "create_by")
    private String createBy;
    @TableField("create_time")
    private String createTime;
    @TableField("last_update_by")
    private String lastUpdateBy;
    @TableField("last_update_time")
    private String lastUpdateTime;
    @TableField("del_flag")
    private BigInteger delFlag;

}
