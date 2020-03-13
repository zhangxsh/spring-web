package com.kkb.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.Inet4Address;
import java.net.UnknownHostException;

@SpringBootApplication
@RestController
public class FortuneEurekaApplication {

	Logger log= LoggerFactory.getLogger(getClass());
	public static void main(String[] args) {
		SpringApplication.run(FortuneEurekaApplication.class, args);
	}
	
	@RequestMapping(value = "/", method = {RequestMethod.HEAD,RequestMethod.GET})
    @ResponseBody
    public String Monitor() throws UnknownHostException {
		log.info("new request");
    	return "天涯何处觅知音********"+ Inet4Address.getLocalHost().getHostAddress();

    }
}
