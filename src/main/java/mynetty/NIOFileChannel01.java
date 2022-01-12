package mynetty;

import io.netty.buffer.ByteBuf;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {

    public static void main(String[] args) throws IOException {
        copyFile();
    }



    public static void copyFile() throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        fileInputStreamChannel.read(byteBuffer);
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();
        byteBuffer.flip();
        fileOutputStreamChannel.write(byteBuffer);
    }

    public static void readFile() throws IOException {

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));
    }

    public static void writeFile() throws IOException {
        String str = "sdifjsdkfj";

        FileOutputStream fileOutputStream = new FileOutputStream("1.txt");

        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        fileOutputStream.close();
    }


}
