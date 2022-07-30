package com.gw.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor//生成一个无参构造函数
@Data
@Accessors(chain=true)
public class PageInfo<T> implements Serializable {

    //回传draw
    private long draw;
    //总记录数
    private long recordsTotal;
    //过滤后的总记录数
    private long recordsFiltered;
    //数据
    private List<T> data;
    //错误信息
    private String error;

}
