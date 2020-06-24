package com.kkb.eureka.io.nio;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xianshuangzhang
 * @date 2020/6/24 20:47
 */
public class NIOClient {

    public static void main(String[] args) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            es.submit(() -> {
                try {
                    send();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(100 * 1000);
    }

    private static void send() throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        //2.连接服务器
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8100));
        Thread.sleep(5 * 1000);
        //写数据
        String msg = "client " + Thread.currentThread().getId();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(msg.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        socketChannel.shutdownOutput();
        //读数据
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = 0;
        while (true) {
            buffer.clear();
            len = socketChannel.read(buffer);
            if (len == -1)
                break;
            buffer.flip();
            while (buffer.hasRemaining()) {
                bos.write(buffer.get());
            }
        }
        System.out.println("客户端收到:" + new String(bos.toByteArray()));
        socketChannel.close();
    }
}
