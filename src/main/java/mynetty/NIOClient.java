package mynetty;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws IOException {
        client();
    }

    public static void BIOclient() throws IOException {
        Socket socket = new Socket("127.0.0.1", 7878);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("helleo nidie".getBytes());
    }

    public static void client() throws IOException {

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);

        if (!socketChannel.connect(new InetSocketAddress("127.0.0.1", 7878))) {

            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，不会阻塞，做其他是");
            }

        }

        String st = "hello fuck";
        ByteBuffer buffer = ByteBuffer.wrap(st.getBytes());
        socketChannel.write(buffer);
        System.in.read();

    }
}
