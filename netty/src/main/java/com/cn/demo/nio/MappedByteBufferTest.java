package com.cn.demo.nio;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");
        /**
         *
         */
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(1,(byte)'Y');
        map.put(3,(byte)'A');

        randomAccessFile.close();
        System.out.println("执行结束");


    }
}
