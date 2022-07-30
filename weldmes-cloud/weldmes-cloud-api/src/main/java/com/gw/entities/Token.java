package com.gw.entities;

import lombok.Data;

import java.io.Serializable;

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
