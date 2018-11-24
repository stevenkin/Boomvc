package me.stevenkin.boomvc.mvc.kit;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonKit {

    public static Object fromJson(String json, Type type) {
        if(String.class.equals(type))
            return json;
        return new Gson().fromJson(json, type);
    }

    public static String toJson(Object object, Type type){
        if(String.class.equals(type))
            return (String) object;
        return new Gson().toJson(object, type);
    }

}
