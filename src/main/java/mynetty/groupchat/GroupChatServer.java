package mynetty.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {

    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static int PORT = 7878;

    public GroupChatServer() {

        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {

        } finally {

        }
    }

    public void listen() {
        try {
            while (true) {
                int count = selector.select(1000);
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "上线");
                        }
                        if (selectionKey.isReadable()) {
                            // 待处理 ，专门写方法...
                            readData(selectionKey);
                        }
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待..." + count);
                }
            }
        } catch (Exception e) {

        } finally {

        }
    }

    /**
     * 读取客户端信息
     * @param selectionKey
     */
    public void readData(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;

        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(buffer);
            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println("from 客户端：" + msg);
                sendInfoToOtherClients(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了...");
                selectionKey.cancel();
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {

        }
    }

    /**
     * 消息转发给其他用户
     * @param msg
     * @param self
     * @throws IOException
     */
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        for (SelectionKey key : selector.keys()) {
            Channel channel = key.channel();

            if (channel instanceof SocketChannel && channel != self) {
                SocketChannel dest = (SocketChannel) channel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
