package com.gw.config;

/**
 * @Author zhanghan
 * @Date 2021/7/20 14:22
 * @Description
 */
public class Test {

    public static void main(String[] args) {
        String s = "/task/list";

        s = s.substring(1);

        s = s.substring(0,s.indexOf("/"));

        System.out.println(s);
    }
}
