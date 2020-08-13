package com.kkb.eureka.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RedisClient {

	private Socket socket;
	private BufferedReader reader;
	private PrintWriter pw;

	public RedisClient(String host,Integer port) {

		// 创建socket,建立与redis服务器的连接
		try {
			socket = new Socket(host, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 初始化输入流
			pw = new PrintWriter(socket.getOutputStream()); // 初始化输出流
		}  catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 对redis的操作
	 * @param command 发送给redis的命令
	 */
	public void operate(String command) {

		// get hello

		/*
		 * *2
		 * $3
		 * get
		 * $5
		 * hello
		 */

		String []params = command.split(" "); // 按空格切分多个字符

		pw.println("*" + params.length); // 发送参数的数量

		for(String param : params) {
			pw.println("$" + param.length()); // 发送参数的字符数
			pw.println(param);
		}

		pw.flush();

		// 读取redis服务器端响应的数据
		try {
			String firstLine = reader.readLine();

			char status = firstLine.charAt(0);

			if(status == '*') {
				Integer responseParamCount = Integer.parseInt(firstLine.substring(1, 2));
				for(int i = 0;i<responseParamCount * 2 ;i++) {
					System.out.println(reader.readLine());
				}
			} else if(status == '$') {
				System.out.println(reader.readLine());
			} else {
				System.out.println(firstLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		new RedisClient("127.0.0.1", 6379).operate("hset c a a");;
	}

}
