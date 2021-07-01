package com.gw.entities;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author zhanghan
 * @Date 2021/6/16 14:08
 * @Description token实体类
 */
@Data
public class Token implements Serializable{

    /**
     * 登录时返回的token
     */
    private Serializable token;

    public Token(Serializable token){
        this.token=token;
    }
}
