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
public class WelderInfo implements Serializable {
    private Long id;
    private String welderName;
    private String welderNo;
    private Byte status;
    private Long deptId;
    private Byte gender;
    private String cellphone;
    private Byte rank;
    private Byte certification;
    private String remarks;
    private SysDictionary sysDictionary;
    private SysDept sysDept;
}
