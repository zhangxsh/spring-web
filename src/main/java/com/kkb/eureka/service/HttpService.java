package com.kkb.eureka.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author alexzhang
 * @date 2021-03-16 5:17 下午
 */
@Service
public class HttpService {

    public String request(String requestUrl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;

        try {
            /** 创建远程url连接对象 */
            URL url = new URL(requestUrl);

            /** 通过远程url对象打开一个连接，强制转换为HttpUrlConnection类型 */
            connection = (HttpURLConnection) url.openConnection();

            /** 设置连接方式：GET */
            connection.setRequestMethod("GET");
            /** 设置连接主机服务器超时时间：15000毫秒 */
            connection.setConnectTimeout(15000);
            /** 设置读取远程返回的数据时间：60000毫秒 */
            connection.setReadTimeout(60000);

            /** 设置通用的请求属性 */
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            /** 发送GET方式请求，使用connet方法建立和远程资源之间的实际连接即可 */
            connection.connect();

            /*-------------------------->*/
            /** 获取所有相应头字段 */
            Map<String, List<String>> map = connection.getHeaderFields();
            /** 遍历响应头字段 */
            for (String key : map.keySet()) {
                System.out.println(key + "---------->" + map.get(key));
            }
            /* <-------------------------- */

            /** 请求成功：返回码为200 */
            if (connection.getResponseCode() == 200) {
                /** 通过connection连接，获取输入流 */
                is = connection.getInputStream();
                /** 封装输入流is，并指定字符集 */
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                /** 存放数据 */
                StringBuffer sbf = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sbf.append(line);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /** 关闭资源 */
            try {

                if (null != br) {
                    br.close();
                }

                if (null != is) {
                    is.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

/** 关闭远程连接 */
// 断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
// 固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些
            connection.disconnect();

            System.out.println("--------->>> GET request end <<<----------");
        }
        return result;
    }
}
