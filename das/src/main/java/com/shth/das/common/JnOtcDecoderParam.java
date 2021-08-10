package com.shth.das.common;

import lombok.Data;

/**
 * @description: OTC输入参数实体类
 * @author: Shan Long
 * @create: 2021-08-08
 */
@Data
public class JnOtcDecoderParam {

    /**
     * 16进制字符串
     */
    private String str;

    /**
     * 客户端IP
     */
    private String clientIp;

}
