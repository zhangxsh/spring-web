package com.kkb.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class FortuneEurekaApplication {
	static Logger log = LoggerFactory.getLogger(FortuneEurekaApplication.class);

	public static void main(String[] args) throws InterruptedException {

		ExecutorService p = Executors.newFixedThreadPool(100);

		List sockets= new ArrayList<>();
		IntStream.range(1, 2).forEach(i -> {
			p.submit(() -> {
						try {
							Socket ss = new Socket("192.168.1.222", 80);
							InputStream in=ss.getInputStream();
							OutputStream outputStream = ss.getOutputStream();
							outputStream.write(content().getBytes());
							outputStream.flush();
							log.info("{} send ok!",i);
							InputStream is=ss.getInputStream();
							BufferedInputStream streamReader = new BufferedInputStream(is);
							BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
							String line = null;
							while((line = bufferedReader.readLine())!= null)
							{
								System.out.println("[response is:]"+line);
							}

							sockets.add(ss);
							Thread.sleep(120*1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			);
		});
		Thread.sleep(150 * 1000);
	}

	public static String content2(){
		return "a";
	}

	public static String content(){
		StringBuffer sb = new StringBuffer("GET /user?time=1 HTTP/1.1\r\n");
		// 以下为请求头
		sb.append("Host: www.test.com\r\n");
		sb.append("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0\r\n");
		sb.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n");
		sb.append("Accept-Language: zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		sb.append("Accept-Encoding: \r\n");
		sb.append("Connection: keep-alive\r\n");
		sb.append("Upgrade-Insecure-Requests: 1\r\n");
		sb.append("\r\n");
		System.out.println(sb.toString());
		return sb.toString();
	}
}
