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
public class NettyHttpProxyHandler extends ChannelHandlerAdapter{


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
            boolean async = params.containsKey("async");
//            String flowName = params.containsKey("flow") ? params.get("flow").get(0) : null;
//
//            String version = params.containsKey("version") ? params.get("version").get(0) : null;
//
//            Map<String, Object> flowParam = new HashMap<String, Object>();
//            params.remove("flow");
//            params.remove("version");
//            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
//                flowParam.put(entry.getKey(), entry.getValue().get(0));
//            }
            final boolean keepAlive = isKeepAlive(req);
            final long start = System.currentTimeMillis();
            String content = "hello world";
            FullHttpResponse responseOk = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
            sendResponse(responseOk, keepAlive, ctx);
//            if(async) {
//                Future<String> echoFuture = echoService.asyncEcho("async hello world");
//                echoFuture.onComplete(new OnComplete<String>() {
//                    @Override
//                    public void onComplete(Throwable failure, String success) throws Throwable {
//                        long end = System.currentTimeMillis();
//                        String content = success + ",cost:" + (end - start) + " ms";
//                        FullHttpResponse responseOk = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
//                        sendResponse(responseOk, keepAlive, ctx);
//                    }
//                }, Context.context());
//            }else{
//                String content = echoService.syncEcho("sync hello world");
//                long end = System.currentTimeMillis();
//                content = content + ",cost " + (end - start) + " ms";
//                FullHttpResponse responseOk = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content.getBytes()));
//                sendResponse(responseOk, keepAlive, ctx);
//            }

////
////                            sendResponse(responseOk, keepAlive, ctx0);
//            if (StringUtils.isBlank(flowName) || StringUtils.isBlank(version)) {
//                FullHttpResponse invalidParamResponse = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST, Unpooled.wrappedBuffer("Must param:flow,version".getBytes()));
//                sendResponse(invalidParamResponse, false, ctx);
//            } else {
//                final long flow_create_start = TimeUtil.nowMic();
//                Flow flow = flowFactory.create(flowName, version);
//                final long flow_create_end = TimeUtil.nowMic();
//
//
//                flow.getContext().putAll(flowParam);
//                final long flow_start_start = TimeUtil.nowMic();
//                final FlowFuture<String> future = flow.start();
//                final long flow_start_end = TimeUtil.nowMic();
//                FullHttpResponse response = null;
//                try {
//                    String result = future.get();
//                    long flow_run_end = TimeUtil.nowMic();
//                    response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(result == null ? "".getBytes() : result.toString().getBytes()));
//                    sendResponse(response,keepAlive,ctx);
//                    logger.info("flowCreate:{},flowStart:{},flowRun:{},total request:{}", flow_create_end - flow_create_start, flow_start_end - flow_start_start, flow_run_end-flow_start_end,TimeUtil.nowMic() - request_start);
//                } catch (Exception e) {
//                    logger.error(e.getMessage(), e);
//                    response = new DefaultFullHttpResponse(HTTP_1_1,HttpResponseStatus.OK, Unpooled.wrappedBuffer(e.toString().getBytes()));
//                    sendResponse(response, false, ctx);
//                }
//
////                final long flow_start_end = TimeUtil.nowMic();
////                final ChannelHandlerContext ctx0 = ctx;
////
////                future.addListener(new FlowListener() {
////                    @Override
////                    public void operatorComplete(FlowFuture f) {
////                        if (f.isSuccess()) {
////                            Object result = null;
////                            try {
////                                result = future.get();
////                            } catch (InterruptedException e) {
////                                //
////                            }
////                            long flow_run_end = TimeUtil.nowMic();
////                            FullHttpResponse responseOk = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result == null ? "".getBytes() : result.toString().getBytes()));
////
////                            sendResponse(responseOk, keepAlive, ctx0);
////                            logger.info("flowCreate:{},flowStart:{},flowRun:{},total request:{}", flow_create_end - flow_create_start, flow_start_end - flow_start_start, flow_run_end-flow_start_end,TimeUtil.nowMic() - request_start);
////                        } else {
////                            Throwable cause = f.getCause();
////                            FullHttpResponse responseError = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(cause.toString().getBytes()));
////                            sendResponse(responseError, false, ctx0);
////                        }
////                    }
////                });
//
//            }


        }

    }


    private void sendResponse(FullHttpResponse response, boolean keepAlive,ChannelHandlerContext ctx) {
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