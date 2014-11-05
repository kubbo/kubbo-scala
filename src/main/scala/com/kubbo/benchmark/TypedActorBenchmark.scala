package com.kubbo.benchmark

import java.util.concurrent.Executors

import akka.actor.{Identify, ActorSystem, TypedActor}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.pattern.Patterns
/**
 * Created by zhu on 2014/10/9.
 */

object TypedActorBenchmark extends App {

  val system = ActorSystem("typedSystem", ConfigFactory.parseString("akka.remote.netty.tcp.port=0").withFallback(ConfigFactory.load("typed.conf")))

  val typed = TypedActor(system)
//  val impl = new EchoServiceImpl()
//  val actorNumber = 64;
//  var routees: List[String] = List()
//
//  for (i <- 1 to actorNumber) {
//    val p = typed.typedActorOf(TypedProps[EchoService](classOf[EchoService], {
//      impl
//    }))
//    val actorRef = typed.getActorRefFor(p)
//    println("actorRef:" + actorRef)
//    routees = actorRef.path.toStringWithoutAddress :: routees
//  }
//  println(routees)
//
//
//  val router = system.actorOf(RoundRobinGroup(routees).props(),"router")
//  println("router:" + router)
  val routerSel = system.actorSelection("akka.tcp://typedSystem@127.0.0.1:2222/user/router")
  println("routerSel:"+routerSel)
  val result = Await.result(Patterns.ask(routerSel, Identify,100000L),Duration("100s"))
  println("result:"+result)
//  val proxy:EchoService = typed.typedActorOf(TypedProps[EchoService], "remote-router")
//  proxy.syncEcho("hello")

//  val echoService: EchoService = typed.typedActorOf(TypedProps[EchoService], router)


  val concurrent = 32;

  val executor = Executors.newFixedThreadPool(concurrent)
  val max = 100000

//
//  var testNum = 1000
//  for (t <- 1 to testNum) {
//    val start = System.currentTimeMillis()
//    val counter = new AtomicInteger(max)
//    val latch = new CountDownLatch(concurrent)
//    for (i <- 1 to concurrent) {
//      executor.execute(new Runnable() {
//        override def run(): Unit = {
//          while (counter.decrementAndGet() > 0) {
//            echoService.syncEcho("hello", 0, false)
//          }
//          latch.countDown()
//        }
//
//      })
//    }
//
//    latch.await()
//    val end = System.currentTimeMillis()
//
//    println("cost:" + (end - start) + " ms,opt/s:" + (max * 1000 / (end - start)))
//  }

  System.exit(0)


}
