package com.kubbo.proxy.http

import com.kubbo.proxy.http.scan.ScanHttpProxy

/**
 * Created by zhu on 2014/9/28.
 */
object HttpBootstrap extends App {

  val httpProxy:HttpProxy= ScanHttpProxy()
  httpProxy.start()

}
