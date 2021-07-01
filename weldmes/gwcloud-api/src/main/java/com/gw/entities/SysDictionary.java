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
@TableName(value = "sys_dictionary")//指定表名
public class SysDictionary implements Serializable {
    @TableId(type = IdType.AUTO)      //主见注解，数据库ID自增
    private BigInteger id;
    private String type;
    private String typeName;
    private String value;
    private String valueName;
    private String remarks;
    private String valueNames;
    private String valueNamess;
    private String valueNamesss;
    private String valueNamessss;


 public void setCreateBy(String admin) {
    }

    public void setCreateTime(String format) {
    }
}
