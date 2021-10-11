package com.shth.das.common;

/**
 * @description: 获取MQTT主题名
 * @author: Shan Long
 * @create: 2021-10-09
 */
public class GainTopicName {

    /**
     * 项目名称
     */
    private final static String PROJECT_NAME = "jn";

    /**
     * 根据枚举主题获取当前项目的主题名称（下行）
     *
     * @param name 枚举主题
     * @return 主题名称
     */
    public static String getMqttDownTopicName(DownTopicEnum name) {
        if (name == null) {
            return null;
        }
        return PROJECT_NAME + name.getValue();
    }

    /**
     * 根据枚举主题获取当前项目的主题名称（上行）
     *
     * @param name 枚举主题
     * @return 主题名称
     */
    public static String getMqttUpTopicName(UpTopicEnum name) {
        if (name == null) {
            return null;
        }
        return PROJECT_NAME + name.getValue();
    }

}
