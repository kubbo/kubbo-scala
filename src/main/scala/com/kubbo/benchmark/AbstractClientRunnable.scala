package com.kubbo.benchmark

import java.util.Properties
import java.util.concurrent.{CountDownLatch, CyclicBarrier}

/**
 * Created by zhu on 2014/10/3.
 */
class AbstractClientRunnable(val barrier:CyclicBarrier,
                             val latch:CountDownLatch,
                             val requestNum:Int)extends ClientRunnable{
  private final val MAX:Int = 1024

  private var running:Boolean = false
  private val responseSpreads:Map[String,String] = Map()
  private val errorTPS:Map[String,String] = Map()
  private val errorResponseTimes:Map[String,String] = Map();
  private val tps:Map[Integer,Long] = Map()
  private val responseTimes:Map[Integer,Long] = Map()
  private var startTime:Long = _

  protected val properties:Properties = new Properties()





  override def getResult: Map[String, String] = {
    println("HELLO")
    return null;
  }


  override def run(): Unit = {

  }
}
