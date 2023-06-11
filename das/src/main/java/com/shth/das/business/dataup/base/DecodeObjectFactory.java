package com.shth.das.business.dataup.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 享元模式
 */
public class DecodeObjectFactory {

    /**
     * K:ClassName
     * V:bean实例
     */
    private final Map<String, Object> objectMap = new HashMap<>();

    private final Map<String, List<Object>> listMap = new HashMap<>();

    private static final String HASH_MAP_NAME = "HashMapName";

    public <T> T getObject(Class<T> clazz) throws Exception {
        String name = clazz.getName();
        Object object;
        //如果有，直接取出
        if (objectMap.containsKey(name)) {
            object = objectMap.get(name);
        }
        //如果没有，就创建一个并放入缓存池
        else {
            object = clazz.getConstructor().newInstance();
            objectMap.put(name, object);
        }
        return (T) object;
    }

    public <T> List<T> getList(Class<T> clazz) {
        String name = clazz.getName();
        //如果有，直接取出
        if (listMap.containsKey(name)) {
            return (List<T>) listMap.get(name);
        }
        //如果没有，就创建一个并放入缓存池
        else {
            List<Object> list = new ArrayList<>();
            listMap.put(name, list);
            return (List<T>) list;
        }
    }

    public Map<String, String> getMap() {
        //如果有，直接取出
        if (objectMap.containsKey(HASH_MAP_NAME)) {
            return (Map<String, String>) objectMap.get(HASH_MAP_NAME);
        }
        //如果没有，就创建一个并放入缓存池
        else {
            Map<String, String> map = new HashMap<>();
            objectMap.put(HASH_MAP_NAME, map);
            return map;
        }
    }

    public int getObjectMapSize() {
        return objectMap.size();
    }

    public int getListMapSize() {
        return listMap.size();
    }

}
