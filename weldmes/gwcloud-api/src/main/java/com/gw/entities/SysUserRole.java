package com.gw.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
public class SysUserRole implements Serializable {
    private long id;
    private Long userId;
    private Long roleId;
    private String createBy;
    private Timestamp createTime;
    private String lastUpdateBy;
    private Timestamp lastUpdateTime;


}
