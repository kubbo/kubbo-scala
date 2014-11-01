package com.kubbo.benchmark

import akka.actor.{TypedProps, TypedActor, ActorSystem}
import com.kubbo.demo.{EchoServiceImpl, EchoService}
import com.typesafe.config.ConfigFactory

/**
 * Created by zhu on 2014/10/9.
 */

object TypedActorBenchmark extends App{

  val system = ActorSystem("typedSystem", ConfigFactory.empty())
  val typed = TypedActor(system)
  val impl = new EchoServiceImpl()

  val echoService:EchoService = typed.typedActorOf(TypedProps[EchoService](classOf[EchoService], {
    impl
  }))

  val size = 100
  val start = System.currentTimeMillis()
  for(i <- 0 until size){
    echoService.asyncEcho("hello world",1000,true)
  }
  println("xxxx")
  Thread.sleep(Integer.MAX_VALUE)
  val mid = System.currentTimeMillis()

  println("cost:" + (mid - start)+",opt="+(size*1000/(mid-start))+"/s")
  for (i <- 0 until size) {
    impl.syncEcho("hello world")
  }
  val end = System.currentTimeMillis()
  println("cost:" + (end - mid) + ",opt=" + (size * 1000 / (end - mid)) + "/s")
  System.exit(0)








}
