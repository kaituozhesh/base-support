package com.ktz.sh.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class JSONUtils {
    /**
     * Bean对象转JSON
     *
     * @param object
     * @param dataFormatString
     * @return
     */
    public static String beanToJson(Object object, String dataFormatString) {
        if (object != null) {
            if (StringUtils.isEmpty(dataFormatString)) {
                return JSONObject.toJSONString(object);
            }
            return JSON.toJSONStringWithDateFormat(object, dataFormatString);
        } else {
            return null;
        }
    }

    /**
     * Bean对象转JSON
     *
     * @param object
     * @return
     */
    public static String beanToJson(Object object) {
        if (object != null) {
            return JSON.toJSONString(object);
        } else {
            return null;
        }
    }

    /**
     * String转JSON字符串
     *
     * @param key
     * @param value
     * @return
     */
    public static String stringToJsonByFastjson(String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        return beanToJson(map, null);
    }

    /**
     * 将json字符串转换成对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static Object jsonToBean(String json, Object clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }
        return JSON.parseObject(json, clazz.getClass());
    }

    /**
     * json字符串转map
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return new HashMap<>();
        }
        return JSON.parseObject(json, Map.class);
    }

    /**
     * fastjson 1.2.44版本判断json方式
     *
     * @param param
     * @return
     */
    public static String toJsonString44(Object param) {
        if (param == null) {
            return "null";
        } else {
            String s = JSON.toJSONString(param, SerializerFeature.WriteMapNullValue);
            Object obj = JSON.parse(s);
            return obj instanceof JSONObject || obj instanceof JSONArray ? s : param.toString();
        }
    }

    /**
     * fastjson 1.2.62版本判断json方式
     *
     * @param param
     * @return
     */
    public static String toJsonString62(Object param) {
        if (param == null) {
            return "null";
        } else {
            String s = JSON.toJSONString(param, SerializerFeature.WriteMapNullValue);
            return JSON.isValidObject(s) || JSON.isValidArray(s) ? s : param.toString();
        }
    }

    public static JSONObject MapToJson(Map m) {
        JSONObject json = new JSONObject(m);
        return json;
    }

    public static String JsonToString(JSONObject json) {
        return json.toJSONString();
    }


}