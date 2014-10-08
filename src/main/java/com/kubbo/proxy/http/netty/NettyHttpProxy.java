package com.kubbo.proxy.http.netty;

import com.kubbo.proxy.http.HttpProxy;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * <title>NettyHttpProxy</title>
 * <p></p>
 *
 * @author zhuwei
 *         2014/10/8
 */
public class NettyHttpProxy implements HttpProxy {

    @Override
    public void start(int port)  {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 4);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyHttpProxyHandlerInitializer());

            Channel ch = null;
            try {
                ch = b.bind(port).sync().channel();
                System.out.println("NettyHttpProxy start!!!");
                ch.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}