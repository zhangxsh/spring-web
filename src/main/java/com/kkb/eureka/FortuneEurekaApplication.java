package com.kkb.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.net.Inet4Address;
import java.net.UnknownHostException;

@SpringBootApplication
@RestController
public class FortuneEurekaApplication {

	Logger log= LoggerFactory.getLogger(getClass());
	public static void main(String[] args) {
		SpringApplication.run(FortuneEurekaApplication.class, args);
	}
	
	@RequestMapping(value = "/user", method = {RequestMethod.HEAD,RequestMethod.GET})
	@ResponseBody
	public String user(long time) throws UnknownHostException {
		log.info("new request");
		if(time>0L){
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return "user:"+time+","+ Inet4Address.getLocalHost().getHostAddress();
	}

	@RequestMapping(value = "/sys", method = {RequestMethod.HEAD,RequestMethod.GET})
	@ResponseBody
	public String sys(long time) throws UnknownHostException {
		log.info("new request");
		if(time>0L){
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return "sys:"+time+","+ Inet4Address.getLocalHost().getHostAddress();
	}

	@RequestMapping(value = "/tx", method = {RequestMethod.HEAD,RequestMethod.GET})
	@ResponseBody
	public String tx(long time) throws UnknownHostException {
		log.info("new request");
		if(time>0L){
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return "tx:"+time+","+ Inet4Address.getLocalHost().getHostAddress();
	}
}
