package com.kubbo.proxy.http.netty;

import com.kubbo.demo.EchoService;
import com.kubbo.rpc.Ref;
import com.kubbo.rpc.akka.Reference;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Future;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.*;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * <title>NettyHttpProxyHandler</title>
 * <p></p>
 *
 * @author zhuwei
 *         2014/10/8
 */
public class NettyHttpProxyHandler extends ChannelHandlerAdapter {


    private static final Logger logger = LoggerFactory.getLogger(NettyHttpProxyHandler.class);

    private Ref ref = Reference.get();
    private EchoService echoService = ref.getRef(EchoService.class, "test", "1.0.0");

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {

            HttpRequest req = (HttpRequest) msg;

            if (is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }

            QueryStringDecoder decoder = new QueryStringDecoder(req.getUri(), Charset.forName("UTF-8"));
            Map<String, List<String>> params = decoder.parameters();
            String method = params.containsKey("method") ? params.get("method").get(0) : "sync";
            final boolean verbose = params.containsKey("verbose") ? Boolean.parseBoolean(params.get("verbose").get(0)) : Boolean.FALSE;
            final long sleep = params.containsKey("sleep") ? Long.parseLong(params.get("sleep").get(0)) : 0;
            final boolean keepAlive = isKeepAlive(req);


            if ("async".equals(method)) {
                final long start = System.nanoTime();
                Future<String> echoFuture = echoService.asyncEcho("async hello world",sleep,verbose);
                long end = System.nanoTime();
                String content = "hello" + ",cost:" + (end - start) + " ms";

                if (verbose) {
                    logger.info(content);
                }
                FullHttpResponse responseOk = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
                sendResponse(responseOk, keepAlive, ctx);
//                echoFuture.onComplete(new OnComplete<String>() {
//                    @Override
//                    public void onComplete(Throwable failure, String success) throws Throwable {
//                        long end = System.currentTimeMillis();
//                        String content = success + ",cost:" + (end - start) + " ms";
//                        if (verbose) {
//                            logger.info(content);
//                        }
//                        FullHttpResponse responseOk = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
//                        sendResponse(responseOk, keepAlive, ctx);
//                    }
//                }, Context.context());
            } else if ("sync".equals(method)) {
                final long start = System.nanoTime();
                String content = echoService.syncEcho("sync hello world",sleep,verbose);
                long end = System.nanoTime();
                content = content + ",cost " + (end - start) + " ms";
                if (verbose) {
                    logger.info(content);
                }
                FullHttpResponse responseOk = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
                sendResponse(responseOk, keepAlive, ctx);
            } else if ("void".equals(method)) {
                String content = "void hello world";
                final long start = System.nanoTime();
                echoService.voidEcho(content, sleep, verbose);
                long end = System.nanoTime();
                if (verbose) {
                    logger.info(content + ",cost:" + (end - start));
                }

                FullHttpResponse responseOk = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
                sendResponse(responseOk, keepAlive, ctx);
            } else if ("none".equals(method)) {
                String content = "none hello world";
                FullHttpResponse responseOk = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
                sendResponse(responseOk, keepAlive, ctx);
            }
        }

    }


    private void sendResponse(FullHttpResponse response, boolean keepAlive, ChannelHandlerContext ctx) {
        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

        if (!keepAlive) {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, Values.KEEP_ALIVE);
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}