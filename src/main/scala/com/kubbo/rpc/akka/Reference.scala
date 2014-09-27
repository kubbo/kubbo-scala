package com.kubbo.rpc.akka

import akka.actor.{TypedProps, ActorRef, ActorSystem, TypedActor}
import akka.cluster.routing.{ClusterRouterGroup, ClusterRouterGroupSettings}
import akka.routing.RoundRobinGroup
import com.kubbo.rpc.Ref
import com.kubbo.rpc.akka.Constants.SYSTEM
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

/**
 * Created by zhu on 2014/9/27.
 */
class Reference extends Ref {

  val system = ActorSystem(SYSTEM, ConfigFactory.parseString(Constants.CONFIG_CONSUMER_ROLE)
    .withFallback(ConfigFactory.load()))

  val typed = TypedActor(system)

  var refMap:Map[ProviderConfig,_] = Map()


  override def getRef[T<:AnyRef](clazz: Class[T], group: String, version: String): T = {
      val config = new ProviderConfig(clazz,group,version)
      val ref = refMap.get(config)
      if(ref.isEmpty){
        val akkaPath = config.toAkkaPath.intern()
        akkaPath.synchronized{
          if(refMap.contains(config)){
            return refMap.get(config).get.asInstanceOf[T]
          }
          val routees = List(akkaPath);
          val router:ActorRef = system.actorOf(
            ClusterRouterGroup(RoundRobinGroup(routees),
              ClusterRouterGroupSettings(Constants.PROVIDER_TOTAL_INSTANCE,
          routees,true,Option(Constants.PROVIDER_ROLE))).props())
          val proxy = typed.typedActorOf(TypedProps[T](clazz),actorRef = router)
          refMap += config->proxy
        }
      }
    return refMap.get(config).get.asInstanceOf[T]

  }
}

object Reference{
  val ref :Reference = new Reference
  def get():Reference = {
    ref
  }

  def context():ExecutionContext ={
    ref.system.dispatcher
  }
}
