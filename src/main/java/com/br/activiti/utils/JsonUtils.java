package com.br.activiti.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class JsonUtils {
    private static Gson g;

    static{
        g = new GsonBuilder().create();
    }

    public static String toJsonString(Object o ){
        return g.toJson(o);
    }

    public static Map<String, Object> toMap(String jsonStr){
        Map<String, Object> map = g.fromJson(jsonStr, Map.class);
        return map;
    }
}
