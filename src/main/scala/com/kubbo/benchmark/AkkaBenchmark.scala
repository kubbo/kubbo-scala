package com.kubbo.benchmark

import java.util.concurrent.CountDownLatch

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
 * Created by zhu on 2014/10/9.
 */

case object Run

case object Msg

class Destination extends Actor {
  override def receive: Receive = {
    case Msg => sender ! Msg
  }
}

class Client(actor: ActorRef, latch: CountDownLatch, repeat: Long) extends Actor {

  var sent = 0L
  var received = 0L

  def receive = {
    case Msg ⇒
      received += 1
      if (sent < repeat) {
        actor ! Msg
        sent += 1
      } else if (received >= repeat) {
        latch.countDown()
      }
    case Run ⇒
      for (i ← 0L until repeat) {
        actor ! Msg
        sent += 1
      }
  }
}

object AkkaBenchmark extends App {
  val system = ActorSystem("bench",ConfigFactory.empty())

  val latch = new CountDownLatch(1)
  val dest = system.actorOf(Props[Destination], "dest")
  val client = system.actorOf(Props[Client]{
    new Client(dest,latch,1000000)
  })

  client!Run
  val start = System.currentTimeMillis()
  latch.await()
  val end  = System.currentTimeMillis()

  println("cost :" + (end - start))


}
