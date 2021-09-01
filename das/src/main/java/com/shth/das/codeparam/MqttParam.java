package com.shth.das.codeparam;

import lombok.Data;

/**
 * @description: MqttParam
 * @author: Shan Long
 * @create: 2021-09-01
 */
@Data
public class MqttParam {

    /**
     * 主题
     */
    private String topic;

    /**
     * 消息体
     */
    private String message;
}
