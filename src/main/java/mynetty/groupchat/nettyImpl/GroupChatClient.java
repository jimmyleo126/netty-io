package mynetty.groupchat.nettyImpl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {

    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 得到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            // 向pipeline加入解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            // 向pipeline加入编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            // 加入自定义handler
                            pipeline.addLast(null);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            // 得到channel
            Channel channel = channelFuture.channel();
            System.out.println("----------" + channel.localAddress() + "----------");
            // 客户端需要输入信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String var = scanner.nextLine();
                channel.writeAndFlush(var + "\r\n");
            }
        } finally {
            group.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        new GroupChatClient("127.0.0.1", 7878);
    }
}
