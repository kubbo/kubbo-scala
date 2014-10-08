package com.kubbo.proxy.http

import com.kubbo.proxy.http.netty.NettyHttpProxy

/**
 * Created by zhu on 2014/9/28.
 */
object HttpBootstrap extends App {

  val httpProxy: HttpProxy = new NettyHttpProxy()
  httpProxy.start(8080)

}
