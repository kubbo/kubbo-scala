package com.kubbo.proxy.http.netty;

import com.kubbo.proxy.http.HttpProxy;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <title>NettyHttpProxy</title>
 * <p></p>
 *
 * @author zhuwei
 *         2014/10/8
 */
public class NettyHttpProxy implements HttpProxy {

    private static final Logger logger = LoggerFactory.getLogger(NettyHttpProxy.class);
    private static final String CONFIG = "proxy.conf";
    private NettyProxyConfig config = NettyProxyConfig.load(CONFIG);


    @Override
    public void start(int port)  {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(config.getWorkerCount());
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyHttpProxyHandlerInitializer());

            Channel ch = null;
            try {
                ch = b.bind(port).sync().channel();
                logger.info("NettyHttpProxy start!!!");

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