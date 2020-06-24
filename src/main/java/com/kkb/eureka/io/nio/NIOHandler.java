package com.kkb.eureka.io.nio;

import com.google.common.base.Stopwatch;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author xianshuangzhang
 * @date 2020/6/24 20:21
 */
public class NIOHandler implements Runnable {
    SelectionKey selectionKey;
    public NIOHandler(SelectionKey selectionKey){
        this.selectionKey=selectionKey;
    }
    @Override
    public void run() {
        try {
            Stopwatch sw=Stopwatch.createStarted();
            System.out.println("线程:" + Thread.currentThread().getId() + "开始处理socket");
            readFromSelectionKey(selectionKey);

            System.out.println("线程:" + Thread.currentThread().getId() + "处理完成,耗时:"+sw.elapsed(TimeUnit.MILLISECONDS));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private  void readFromSelectionKey(SelectionKey selectionKey) throws Exception{
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int count = 0;
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        buffer.clear();
        while ((count = channel.read(buffer)) > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.println("线程接收:"+buffer.get());
            }
            buffer.clear();
        }
        if (count < 0) {
            channel.close();
        }
    }
}
