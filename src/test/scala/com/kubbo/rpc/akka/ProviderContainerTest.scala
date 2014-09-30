package com.kubbo.rpc.akka

import com.kubbo.demo.{EchoServiceImpl, EchoService}


/**
 * Created by zhu on 2014/9/27.
 */


object ProviderContainerTest {


  def startProvider = {
    val pContainer = ProviderContainer()
    val test = pContainer.start(classOf[EchoService],new EchoServiceImpl(),"test","1.0.0")
    println(test)
  }

  def startConsumer = {
    val ref = Reference.get()
    val test = ref.getRef(classOf[EchoService],"test","1.0.0")
    Thread.sleep(3000)
    for(i <- 1 to 10){
      val result = test.syncEcho("hello")
      println(result)
      Thread.sleep(1000)
    }

  }
  def main(args: Array[String]) {
    startProvider
    Thread.sleep(1000)
    startConsumer
  }
}
