package com.gw.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
public class MachineWeldGather implements Serializable {
    private long id;
    private long weldId;
    private long gatherId;

}
