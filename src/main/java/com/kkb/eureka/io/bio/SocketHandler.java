package com.kkb.eureka.io.bio;

import com.google.common.base.Stopwatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author xianshuangzhang
 * @date 2020/6/24 19:59
 */
public class SocketHandler implements Runnable {

    Socket ss;

    public SocketHandler(Socket socket) {
        ss=socket;
    }

    @Override
    public void run() {

        Stopwatch sw=Stopwatch.createStarted();
        System.out.println("线程:" + Thread.currentThread().getId() + "开始处理socket");
        //读取客户端的数据
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
            String str = null;
            str = br.readLine();
            System.out.println("线程接收:"+str);
            System.out.println("线程:" + Thread.currentThread().getId() + "处理完成,耗时:"+sw.elapsed(TimeUnit.MILLISECONDS));
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
