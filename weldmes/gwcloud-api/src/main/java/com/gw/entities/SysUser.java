package com.gw.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigInteger;


@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@ToString
@Accessors(chain = true)
@TableName(value = "sys_user")//指定表名
public class SysUser extends Model<SysUser> implements Serializable {
    @TableId(type = IdType.AUTO)        //主见注解，数据库ID自增
    private BigInteger id;
    @TableField("`name`")
    private String name;
    @TableField("`password`")
    private String password;
    private String salt;
    private String email;
    private String mobile;
    @TableField(exist = false)   //不为数据库字段，不存入数据库
    private String userStatus;
    @TableField("`status`")
    private BigInteger status;
    @TableField(value = "dept_id")
    private BigInteger deptId;
    @TableField(value = "create_by")
    private String createBy;
    @TableField("create_time")
    private String createTime;
    @TableField("last_update_by")
    private String lastUpdateBy;
    @TableField("last_update_time")
    private String lastUpdateTime;
    @TableLogic("del_flag")
    private BigInteger delFlag;


}
