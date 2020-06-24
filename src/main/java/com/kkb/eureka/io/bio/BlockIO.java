package com.kkb.eureka.io.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author alexzhang
 * @date 2020/6/24 19:58
 */
public class BlockIO {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService es= Executors.newFixedThreadPool(10);
        for(int i=0;i<10;i++) {
            es.submit(() -> {
                try {
                    send();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(100*1000);
    }

    public static void send() throws Exception{
        Socket socket=new Socket("127.0.0.1", 8100);
        Thread.sleep(5*1000);
        OutputStream outputStream = socket.getOutputStream();
        System.out.println("thread send"+Thread.currentThread().getId());
        String message = "thread:"+Thread.currentThread().getId();
        socket.getOutputStream().write(message.getBytes("UTF-8"));
        socket.shutdownOutput();
    }
}
