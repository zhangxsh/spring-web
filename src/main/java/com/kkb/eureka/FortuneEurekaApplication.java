package com.kkb.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class FortuneEurekaApplication {

	static String content="GET / HTTP/1.1\n"
			+
			"Host: abc.test.com\n" +
			"Accept: */*\n"
+
			"Connection: keep-alive\n" +
			"Accept-Language: zh-cn\n" +
			"Content-Length: 0\n" +
			"Cache-Control: no-cache"
			;

//	static String content="test";
	static Logger log = LoggerFactory.getLogger(FortuneEurekaApplication.class);

	public static void main(String[] args) throws InterruptedException {

		ExecutorService p = Executors.newFixedThreadPool(100);

		IntStream.range(1, 5).forEach(i -> {
			p.submit(() -> {
						try {
							Socket ss = new Socket("192.168.1.222", 80);
							OutputStream outputStream = ss.getOutputStream();
							outputStream.write(content.getBytes());
							outputStream.flush();
							log.info("{} send!",i);
							Thread.sleep(120*1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			);
		});
		Thread.sleep(150 * 1000);
	}
}
