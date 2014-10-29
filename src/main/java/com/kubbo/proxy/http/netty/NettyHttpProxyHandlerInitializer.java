package com.kubbo.proxy.http.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

/**
 * <title>NettyProxyHandlernitializer</title>
 * <p></p>
 *
 * @author zhuwei
 *         2014/10/8
 */
public class NettyHttpProxyHandlerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast("codec", new HttpServerCodec());
        p.addLast("aggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
        p.addLast("handler", new NettyHttpProxyHandler());
        p.addLast("read-timeout", new ReadTimeoutHandler(10));
        p.addLast("wrtie-timeout", new WriteTimeoutHandler(10));

    }
}