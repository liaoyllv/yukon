package com.lyl.yukon.common.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

/**
 * google json工具类
 *
 * @author liaoyl
 */
public class JsonUtils {

    private JsonUtils() {
    }

    private static Gson gson;
    private static JsonParser jsonParser;

    static {
        GsonBuilder builder = new GsonBuilder();
        // 打印好看的json格式
        builder.setPrettyPrinting();
        // 基于当前配置创建gson实例
        gson = builder.create();
        // 实例化jsonParser
        jsonParser = new JsonParser();
    }

    /**
     * json字符串转对象
     */
    public static <T> T toObject(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * json字符串转复杂对象
     */
    public static Object toObject(String json, Type type) {
        return gson.fromJson(json, type);
    }

    /**
     * 对象转json字符串
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * String转为jsonObject
     */
    public static JsonObject toJsonObject(String string) {
        return jsonParser.parse(string).getAsJsonObject();
    }

}
