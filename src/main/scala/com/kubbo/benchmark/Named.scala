package com.kubbo.benchmark

import akka.actor.{ActorSystem, ActorRef, TypedActor, TypedProps}
import akka.routing.RoundRobinGroup
import com.typesafe.config.ConfigFactory

import scala.util.Random

/**
 * Created by zhu on 2014/11/1.
 */

trait HasName{

  def name():String
}

class Named extends HasName{
  private val id = Random.nextInt(100)
  def name():String = "name-"+id;
}


object Named {


  def main(args: Array[String]) {
    val system = ActorSystem("xx",ConfigFactory.empty())

    def namedActor(): HasName = TypedActor(system).typedActorOf(TypedProps[Named]())
    // prepare routees
    val routees: List[HasName] = List.fill(5) { namedActor() }

    println(routees)
    val routeePaths = routees map { r =>
      TypedActor(system).getActorRefFor(r).path.toStringWithoutAddress
    }
    // prepare untyped router
    val router: ActorRef = system.actorOf(RoundRobinGroup(routeePaths).props())
    // prepare typed proxy, forwarding MethodCall messages to ‘router‘
    val typedRouter: HasName =
      TypedActor(system).typedActorOf(TypedProps[Named](), actorRef = router)



  }
}
