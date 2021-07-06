package com.gw.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author zhanghan
 * @Date 2021/7/5 10:12
 * @Description  sys_user_role表 实体类
 * @Params
 */
@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
public class SysUserRole implements Serializable {

    /**
     * 主键
     */
    private long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新人
     */
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    private Timestamp lastUpdateTime;


}
