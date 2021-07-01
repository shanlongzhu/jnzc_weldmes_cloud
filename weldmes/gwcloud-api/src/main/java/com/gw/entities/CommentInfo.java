package com.gw.entities;

import lombok.Data;

/**
 * @Author zhanghan
 * @Date 2021/6/30 14:45
 * @Description 评论信息实体类
 */
@Data
public class CommentInfo {

    /**
     * 任务信息主键
     */
    private Long id;

    /**
     * 评论信息
     */
    private String comments;

    /**
     * 评论星级主键
     */
    private int start;
}
