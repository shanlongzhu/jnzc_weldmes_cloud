package com.gw.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
public class WpsNorm  implements Serializable {
    private long id;
    private long wpsLibraryId;
    private int channelNo;
    private Integer initialCondition;
    private Integer fusionControl;
    private Integer unitarySeveral;
    private String controlArc;
    private String controlArcType;
    private Integer softArcSchema;
    private Integer arcCharacter;
    private BigDecimal spotWeldingTime;
    private String weldingStickTexture;
    private String weldingStickTextureType;
    private String weldingStickDiameter;
    private String weldingStickDiameterType;
    private BigDecimal inAdvanceAspirated;
    private BigDecimal hysteresisAspirated;
    private String gases;
    private String gasesType;
    private String weldingProcess;
    private String weldingProcessType;
    private BigDecimal initialEle;
    private BigDecimal initialVol;
    private Integer weldingEle;
    private BigDecimal weldingVol;
    private Integer arcEle;
    private BigDecimal arcVol;
    private BigDecimal initialVolUnitary;
    private BigDecimal weldingVolUnitary;
    private BigDecimal arcVolUnitary;
    private Integer weldingEleAdjust;
    private BigDecimal weldingVolAdjust;
    private Integer arcEleAdjust;
    private BigDecimal arcVolAdjust;
    private Integer alarmsEleMax;
    private Integer alarmsEleMin;
    private BigDecimal alarmsVolMax;
    private BigDecimal alarmsVolMin;
    private SysDictionary sysDictionary;

}
