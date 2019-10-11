package com.br.activiti.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.springframework.util.StringUtils;


public class JsonUtil {

	
	
	private static Gson gson;
	
	static{
		GsonBuilder gb = new GsonBuilder(); 
		gb.disableHtmlEscaping();
		gb.registerTypeAdapter(String.class, new StringConverter());
		//gb.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory());
		gson = gb.create();
	}
	
	private static JsonParser jsonParser = new JsonParser();
	
	public static final String toJson(Object obj) {
		return gson.toJson(obj);
	}
	
	public static final JsonElement parse(String json) {
		return jsonParser.parse(json);
	}
	
	public static final <T> T fromJson(Class<T> clazz, String json) {
		return gson.fromJson(json, clazz);
	}
	
	public static Map<String, Object> toMap(JsonObject json){
        Map<String, Object> map = new HashMap<String, Object>();  
        Set<Entry<String, JsonElement>> entrySet = json.entrySet();  
        for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ){  
            Entry<String, JsonElement> entry = iter.next();  
            String key = entry.getKey();  
            JsonElement value = entry.getValue();  
            if(value instanceof JsonArray)  
                map.put((String) key, toList((JsonArray) value));  
            else if(value instanceof JsonObject)  
                map.put((String) key, toMap((JsonObject) value)); 
			else if (value instanceof JsonNull)
            	map.put((String) key, ""); 
            else{ 
                if(StringUtils.isEmpty(value)){
                	map.put((String) key, "");
                }else{
            	map.put((String) key, value.getAsString());
                }
            }
        }  
        return map;  
    }
	
    public static List<Object> toList(JsonArray json){
        List<Object> list = new ArrayList<Object>();  
        for (int i=0; i<json.size(); i++){  
            Object value = json.get(i);  
            if(value instanceof JsonArray){  
                list.add(toList((JsonArray) value));  
            }  
            else if(value instanceof JsonObject){  
                list.add(toMap((JsonObject) value));  
            }  
            else{  
                list.add(value);  
            }  
        }  
        return list;  
    } 
    
    public static JsonObject toJsonObj(String jsonStr){
		return jsonParser.parse(jsonStr).getAsJsonObject();
	}
	
	public static void main (String args[]) {
//		String json = "{'body':{'noticeDate':'20140728105442','resultMsg':'�ɹ�','fileName':'8614041700006267_001_001_20140728_101011_I','resultCode':'000000','extBatCode':'101011','noticeType':'02'},'head':{'orgNo':'8614041700006267'}}";
//	    Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();
//	    map = fromJson(Map.class,json);
//	    System.out.println(map.get("body"));
//	    System.out.println(map.get("body").get("noticeDate"));
//	    System.out.println(map.get("head"));
	    
	    
	    Map<String,String> a = new HashMap<String, String>();
	    a.put("a", null);
	    a.put("b", "1");
	    System.out.println(toJson(a));
	}
	
	
	
}


class StringConverter implements JsonSerializer<String>, JsonDeserializer<String> {
	@Override
	public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
		if (src == null) {
			return new JsonPrimitive("");
		} else {
			return new JsonPrimitive(src.toString());
		}
	}

	@Override
	public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return json.getAsJsonPrimitive().getAsString();
	}
}