package com.kkb.eureka;

import com.kkb.eureka.service.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class FortuneEurekaApplication {

    @Autowired
    HttpService httpService;
    Logger log = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        SpringApplication.run(FortuneEurekaApplication.class, args);
    }

    @RequestMapping(value = "/{id}/{name}/{log}", method = {RequestMethod.HEAD, RequestMethod.GET})
    @ResponseBody
    public String Monitor(@PathVariable("id") String id, @PathVariable("name") String name, @PathVariable("log") String isLog) throws IOException {
    	String url="https://www.baidu.com";
		if("true".equalsIgnoreCase(isLog)) {
            String content=httpService.request(url);
            log.info("content:{}", content);
		}
        return "天涯何处觅知音********" + Inet4Address.getLocalHost().getHostAddress();
    }

    @RequestMapping(value = "/request", method = {RequestMethod.HEAD, RequestMethod.GET})
    @ResponseBody
    public String request(String id, String name, String isLog) throws IOException {
        return "request" + Inet4Address.getLocalHost().getHostAddress();
    }

}
