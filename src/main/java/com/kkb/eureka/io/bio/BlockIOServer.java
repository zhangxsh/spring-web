package com.kkb.eureka.io.bio;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xianshuangzhang
 * @date 2020/6/24 19:59
 */
public class BlockIOServer {
    public static void main(String[] args) throws Exception {
        ServerSocket ss=new ServerSocket(8100);
        System.out.println("服务器已启动，等到客户端连接");
        while(true){
            //监听并接受客户端连接，注意该方法有阻塞性
            Socket s=ss.accept();
            System.out.println("客户端连接成功！"+s);
            new Thread(new SocketHandler(s)).start();
        }
    }
}
