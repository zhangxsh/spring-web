package com.kkb.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class FortuneEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FortuneEurekaApplication.class, args);
	}
	
	@RequestMapping(value = "/", method = {RequestMethod.HEAD,RequestMethod.GET})
    @ResponseBody
    public String Monitor() {
    	return "天涯何处觅知音......";
    }
}
