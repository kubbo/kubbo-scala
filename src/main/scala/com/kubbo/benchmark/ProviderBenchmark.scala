package com.kubbo.benchmark

import java.util.concurrent.{CountDownLatch, Executors}
import java.util.concurrent.atomic.AtomicInteger

import akka.actor._
import akka.cluster.routing.{ClusterRouterGroupSettings, ClusterRouterGroup}
import akka.pattern.Patterns
import akka.routing.RoundRobinGroup
import com.kubbo.demo.EchoService
import com.kubbo.rpc.akka.Constants
import com.kubbo.rpc.akka.Constants._
import com.typesafe.config.ConfigFactory

/**
 * Created by zhu on 2014/11/1.
 */


object ProviderBenchmark {

  def main(args: Array[String]): Unit = {

    //    val provider: ProviderContainer = ProviderContainer(4)
    //    val echo = provider start(classOf[EchoService], new EchoServiceImpl(), "test", "1.0.0")
    //
    //    Thread.sleep(1000)
    //    val ref = Reference get()
    //    val echoProxy = ref.getRef(classOf[EchoService], "test", "1.0.0")
    //    Thread.sleep(1000)
    val system = ActorSystem(SYSTEM, ConfigFactory.parseString(Constants.CONFIG_CONSUMER_ROLE)
      .withFallback(ConfigFactory.load()))
    val typed = TypedActor(system)

    val routees = List("/user/com.kubbo.demo.EchoService");
    //    //    val ref = system.actorSelection("/user/com.kubbo.demo.EchoService")
    //    val actor = system.actorFor("/user/com.kubbo.demo.EchoService")
    //    val proxy: EchoService = typed.typedActorOf(TypedProps[EchoService](classOf[EchoService]), actor)

    val router: ActorRef = system.actorOf(
      ClusterRouterGroup(RoundRobinGroup(routees),
        ClusterRouterGroupSettings(Constants.PROVIDER_TOTAL_INSTANCE,
          routees, true, Option(Constants.PROVIDER_ROLE))).props())

    try{
      Patterns.ask(router, Identify, 10000)
    }catch{
      case ex:Exception => ex.printStackTrace()

    }

    println(router)

    val proxy: EchoService = typed.typedActorOf(TypedProps[EchoService](classOf[EchoService]), actorRef = router)
    println(proxy)

    try{
      println(proxy.syncEcho("hello", 0, false))
    }catch{
      case ex:Exception => ex.printStackTrace()

    }

//    Thread.sleep(1000)
//
//    while (true) {
//
//      try {
//        proxy.syncEcho("hello", 0, true)
//      } catch {
//        case ex: Exception => ex.printStackTrace()
//      }
//
//
//
//      println("calll")
//
//    }


    //    echoProxy.syncEcho("hello", 1000, true)
    //
        val concurrent = 1;
        val executor = Executors.newFixedThreadPool(concurrent)

        val max = 10000
        val counter = new AtomicInteger(max);
        val latch = new CountDownLatch(concurrent)
        val start = System.currentTimeMillis()

          for (i <- 1 to concurrent) {
            executor.execute(new Runnable {
              override def run(): Unit = {
                while (counter.getAndDecrement() > 0) {
                  try{
                    proxy.syncEcho("aaa", 0,true)
                  }catch {
                    case ex:Exception => ex.printStackTrace()
                  }

                }
                latch.countDown()

              }
            });
          }
        latch.await()
        val end = System.currentTimeMillis()

        println("cost:" + (end - start) + ",opt/s:" + (max * 1000 / (end - start)))
    //    for (i <- 1 to 100) {
    //      echoProxy.asyncEcho("aaa", 1000, true);
    //    }

    Thread.sleep(1000)
    System.exit(0)

  }
}
