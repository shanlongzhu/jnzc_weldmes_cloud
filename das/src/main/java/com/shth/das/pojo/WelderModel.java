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
 * 焊工实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName(value = "welder_info")
public class WelderModel implements Serializable {

    @TableId(type = IdType.AUTO)
    private BigInteger id;
    @TableField("welder_name")
    private String welderName;
    @TableField("welder_no")
    private String welderNo;
    private Integer status;
    @TableField("dept_id")
    private BigInteger deptId;
//    private String idcard;
    private Integer gender;
//    private Integer attribute;
    private String cellphone;
    @TableField("`rank`")
    private Integer rank;
    private Integer certification;

}
