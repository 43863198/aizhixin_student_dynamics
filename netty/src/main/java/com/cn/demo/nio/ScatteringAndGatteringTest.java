package com.cn.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * scattering 将数据写入到Buffer时可以采用数组的形式一次写入
 * Gattering 将数据读取到Buffer,采用buffer数组依次读取
 */
public class ScatteringAndGatteringTest {
    public static void main(String[] args) throws Exception {
        //使用网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7777);
        //绑定端口并启用
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        //默认接受的消息长度为8
        int messageLength = 8;
        //循环读取
        while (true){
            int byteRead = 0;
            while (byteRead < messageLength){
                long read = socketChannel.read(byteBuffers);
                byteRead += read;
                Arrays.asList(byteBuffers).stream().map(buffer -> "position:" + buffer.position()
                + ",limit:" + buffer.limit()).forEach(System.out::println);
            }
            //将数据进行反转进行输出
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            int byteWrite = 0;
            while (byteWrite < messageLength){
                long write = socketChannel.write(byteBuffers);
                byteWrite+=write;
            }
            //将所有buffer清除
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("byteRead:" + byteRead + ",byteWrite:" + byteWrite);
        }
    }
}
