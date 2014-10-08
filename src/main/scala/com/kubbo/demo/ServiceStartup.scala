package com.kubbo.demo

import com.kubbo.rpc.akka.ProviderContainer

/**
 * Created by zhu on 2014/10/1.
 */

object ServiceStartup{
  def main(args: Array[String]) {
    val container = ProviderContainer()
    container start(classOf[EchoService], new EchoServiceImpl(0,false), "test", "1.0.0")
    println("service start")
  }
}