package com.kubbo.benchmark

import akka.actor.{TypedActor, TypedProps, ActorSystem}
import akka.routing.RoundRobinGroup
import com.kubbo.demo.{EchoService, EchoServiceImpl}
import com.typesafe.config.ConfigFactory

/**
 * Created by zhu on 2014/11/5.
 */
object TypedActorBenchmark2 extends App{

  val system = ActorSystem("typedSystem", ConfigFactory.load("typed.conf"))
  val typed = TypedActor(system)
  val impl = new EchoServiceImpl()
  val actorNumber = 64;
  var routees: List[String] = List()

  for (i <- 1 to actorNumber) {
    val p = typed.typedActorOf(TypedProps[EchoService](classOf[EchoService], {
      impl
    }))
    val actorRef = typed.getActorRefFor(p)
    println("actorRef:" + actorRef)
    routees = actorRef.path.toStringWithoutAddress :: routees
  }
  println(routees)


  val router = system.actorOf(RoundRobinGroup(routees).props(),"router")
  println("router:" + router)

  Thread.sleep(Integer.MAX_VALUE)
}
