package com.gw.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain=true)
//没有RealtimeData这个表
public class RealtimeData implements Serializable {
    private Long id;
    private Long fWelderId;
    private String fGatherNo;
    private Long fMachineId;
    private Long fJunctionId;
    private Long fItemId;
    private BigDecimal fElectricity;
    private BigDecimal fVoltage;
    private BigDecimal fRateOfFlow;
    private Integer fStatus;
    private String fWelderNo;
    private String fJunctionNo;
    private String fWeldNo;
    private Integer fChannel;
    private BigDecimal fMaxElectricity;
    private BigDecimal fMinElectricity;
    private BigDecimal fMaxVoltage;
    private BigDecimal fMinVoltage;
    private Long fWelderItemId;
    private Long fJunctionItemId;
    private Long fMachineItemId;
    private BigDecimal fWireFeedRate;
    private BigDecimal fWeldingRate;
    private String fMachineModel;
    private BigDecimal fWireDiameter;
    private Integer fMaterialGas;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String fWeldTime;
    /*private Timestamp fWeldTime1;*/
    private Integer fSolderLayer;
    private Integer fWeldBead;
    private SysDept sysDept;
    private MachineWeldInfo machineWeldInfo;
    private Integer count;
    private Integer count2;
    private Integer count3;
    private Integer count4;
    private Integer count5;
    private BigDecimal utilization;
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Timestamp time;
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Timestamp time2;
    private BigDecimal utilization2;
    private WelderInfo welderInfo;
    private TaskInfo taskInfo;
    private SysDictionary sysDictionary;

}
