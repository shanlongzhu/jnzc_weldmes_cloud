package com.gw.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
public class MachineGatherInfo implements Serializable {
    private long id;
    private String gatherNo;
    private int status;
    private long deptId;
    private String productionDate;
    private String ipPath;
    private String macPath;
    private int protocol;
    private String createBy;
    private String createTime;
    private String lastUpdateBy;
    private String lastUpdateTime;
    private SysDictionary sysDictionary;
    private SysDept sysDept;

}
