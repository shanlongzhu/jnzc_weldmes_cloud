package com.gw.entities;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author zhanghan
 * @Date 2021/7/13 19:09
 * @Description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class RtData implements Serializable {

    private Long id;
    private Integer weldModel;
    private String gatherNo;
    private String welderNo;
    private String weldTime;
    private BigDecimal electricity;
    private BigDecimal voltage;
    private BigDecimal wireFeedRate;
    private BigDecimal presetEle;
    private BigDecimal presetVol;
    private String weldedJunctionNo;
    private Integer weldStatus;
    private BigDecimal wireDiameter;
    private BigDecimal wireMaterialsGases;
    private BigDecimal weldElectricity;
    private BigDecimal weldVoltage;
    private BigDecimal weldEleAdjust;
    private BigDecimal weldVolAdjust;
    private String channelNo;
    private BigDecimal alarmsEleMax;
    private BigDecimal alarmsEleMin;
    private BigDecimal alarmsVolMax;
    private BigDecimal alarmsVolMin;
    private Long machineId;
    private String machineNo;
    private Long machineDeptId;
    private Long gatherId;
    private Long gatherDeptId;
    private Long welderId;
    private String welderName;
    private Long welderDeptId;
    private Long taskId;
    private String taskName;
    private String taskNo;
    private String createTime;

    /**
     * 设备类型标识（0：OTC，1：松下）
     */
    private Integer macFlag;

    /**
     * 任务实际开始时间
     */
    private String taskRealityStartTime;

    /**
     * 任务实际结束时间
     */
    private String taskRealityEndTime;

}
