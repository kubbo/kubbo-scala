package com.kubbo.benchmark

import akka.actor.{ActorSystem, Identify, TypedActor}
import akka.dispatch.OnComplete
import akka.pattern.Patterns
import com.kubbo.rpc.akka.Constants
import com.kubbo.rpc.akka.Constants._
import com.typesafe.config.ConfigFactory

/**
 * Created by zhu on 2014/11/2.
 */
object MyTypedActor {


  def main(args: Array[String]) {
    val system = ActorSystem(SYSTEM, ConfigFactory.parseString(Constants.CONFIG_CONSUMER_ROLE)
      .withFallback(ConfigFactory.load()))
    val typed = TypedActor(system)
    val selection = system.actorSelection("/user/com.kubbo.demo.EchoService")
    var future = Patterns.ask(selection, Identify, 10000)
    future.onComplete(new OnComplete[AnyRef] {

      override def onComplete(failure: Throwable, success: AnyRef): Unit = {
        println(failure)
        println(success)

      }

    })(system.dispatcher)

    Thread.sleep(1000)
    future = Patterns.ask(selection, Identify, 10000)
    future.onComplete(new OnComplete[AnyRef] {

      override def onComplete(failure: Throwable, success: AnyRef): Unit = {
        println(failure)
        println(success)

      }

    })(system.dispatcher)


  }
}
