package com.cnblog.payment.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @description  Map工具类
 * @author AnYuan
 */


public class MapUtil {
    
    public static String sortAndJoinery(String data) {
        
        JSONObject json = JSONObject.parseObject(data);
        List<String> keys = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (String key : json.keySet()) {
            map.put(key, json.getString(key));
            
            if ("sign".equals(key) || StringUtils.isEmpty(map.get(key))) {
                continue;
            }
            keys.add(key);
        }
        
        Collections.sort(keys);
        StringBuilder buf = new StringBuilder();
        
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key);
            if (i == keys.size() - 1) {
                buf.append(key).append("=").append(value);
            } else {
                buf.append(key).append("=").append(value).append("&");
            }
        }
        
        return buf.toString();
    }
}
