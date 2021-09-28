package com.gw.entities;


import lombok.Data;
import java.io.Serializable;



@Data
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
