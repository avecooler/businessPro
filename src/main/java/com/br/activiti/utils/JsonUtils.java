package com.br.activiti.utils;

import com.br.shopping.dao.domain.ShoppingCar;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static Gson g;

    private static JsonParser jsonParser = new JsonParser();

    static{
        g = new GsonBuilder().create();
    }

    public static String toJsonString(Object o ){
        return g.toJson(o);
    }

    public static Map<String, Object> toMap(Object o){
        Map<String, Object> map = toMap(g.toJson(o));
        return map;
    }

    public static Map<String, Object> toMap(String jsonStr){




        Map<String, Object> map = g.fromJson(jsonStr, Map.class);
        return map;
    }



    public  static <T> T  toObject(String jsonStr, Class<T> t) throws ClassNotFoundException {
//        Class t = Class.forName(d.getName());
        TypeToken typeToken = TypeToken.get(t);
        T object =  g.fromJson(jsonStr,typeToken.getType());
        return object;
    }
    /**
     * 8.通过JSONArray.parseArray把json转换为List<T>
     *
     * @param jsonStr
     * @param model
     * @return
     */
    public static <T> List<T> getStringToList(String jsonStr, Class<T> model) throws ClassNotFoundException {
        List<T> list = new ArrayList<T>();
        JsonArray jsonArray = new JsonParser().parse(jsonStr).getAsJsonArray();
        for(JsonElement el : jsonArray){
            T t = g.fromJson(el, model);
            list.add(t);
        }
        return list;
    }


    public static void main(String[] args) throws ClassNotFoundException {
        String json = "[{'customerId':'123','productId':'123'},{'customerId':'123','productId':'124'}]";
        Object o = getStringToList(json, Class.forName("com.br.shopping.dao.domain.ShoppingCar"));
        System.out.println(o.getClass());
    }
}
