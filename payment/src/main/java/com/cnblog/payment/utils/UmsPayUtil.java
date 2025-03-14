package com.cnblog.payment.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class UmsPayUtil {
    
    public static String getSign(String signKey, String data) {
    
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
                buf.append(key).append("=").append(value).append(signKey);
            } else {
                buf.append(key).append("=").append(value).append("&");
            }
        }
        
        try {
            return DigestUtils.sha256Hex(buf.toString().getBytes(StandardCharsets.UTF_8)).toUpperCase();
        }catch (Exception e) {
            throw new RuntimeException("签名失败");
        }
    }
    
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(new String(param.getBytes(),"utf-8"));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
