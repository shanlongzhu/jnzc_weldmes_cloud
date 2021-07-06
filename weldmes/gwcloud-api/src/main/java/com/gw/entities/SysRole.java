package com.gw.entities;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain=true)
public class SysRole implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新人
     */
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    private String lastUpdateTime;

    /**
     * 是否删除  -1：已删除  0：正常
     */
    private int delFlag;

}
