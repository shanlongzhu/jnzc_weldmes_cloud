package com.gw.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain = true)
public class WpsLibrary implements Serializable {
    private long id;
    private String wpsName;
    private byte weldModel;
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
    private SysDictionary sysDictionary;

    //厂家id(字典)
    private Long firmId;

}
