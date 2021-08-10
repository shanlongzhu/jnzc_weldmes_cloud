package com.shth.das.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @description: Decoder和Handler传递的参数
 * @author: Shan Long
 * @create: 2021-08-09
 */
@Data
public class HandlerParam implements Serializable {

    private int key;
    private Map<String,Object> value;

}
