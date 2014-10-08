package com.kubbo.benchmark

import java.util.concurrent.Executors

import com.kubbo.demo.EchoService
import com.kubbo.rpc.akka.Reference

/**
 * Created by zhu on 2014/10/1.
 */
object Benchmark {

  val echoService = Reference.get().getRef(classOf[EchoService], "test", "1.0.0")
  def main(args: Array[String]): Unit = {
    Thread.sleep(2000)
    val threadNum = Runtime.getRuntime.availableProcessors() * 2;
    val start = System.currentTimeMillis()
    val num = 100000;
    val executor = Executors.newFixedThreadPool(threadNum);
    for(i <- 1 to num){
      executor.execute(new Runnable(){
        override def run(): Unit = {
          echoService.syncEcho("hello world")
        }
      })
    }

    println("all start ")
    val end = System.currentTimeMillis()
    println(end-start)
  }
}

