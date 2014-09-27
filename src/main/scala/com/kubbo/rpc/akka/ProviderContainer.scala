package com.kubbo.rpc.akka

import akka.actor.{ActorRef, ActorSystem, TypedActor, TypedProps}
import akka.event.Logging
import akka.routing.{RoundRobinGroup, Routee}
import com.kubbo.rpc.ProviderLifeCycle
import com.typesafe.config.ConfigFactory
import com.kubbo.rpc.akka.Constants._
import scala.collection.immutable.List

/**
 * Created by zhu on 2014/9/26.
 */
class ProviderContainer(val typedActorNum: Int = Constants.TYPED_ACTOR_NUM) extends ProviderLifeCycle {

  val system: ActorSystem = ActorSystem(SYSTEM, ConfigFactory.parseString(CONFIG_PROVIDER_ROLE)
    .withFallback(ConfigFactory.load()))

  val typed = TypedActor(system)
  val logger = Logging.getLogger(system, this)
  var metadataMap: Map[ProviderConfig, ProviderMetadata[_<:AnyRef]] = Map()

  override def start[T <: AnyRef](clazz: Class[T], implements: T, group: String, version: String): T = {

    require(clazz != null)
    require(implements != null)

    if (!implements.isInstanceOf[T]) {
      throw new IllegalArgumentException(implements + " must implements " + clazz)
    }
    val config: ProviderConfig = new ProviderConfig(clazz, implements, group, version)

    val metadata = metadataMap.get(config)

    if (metadata.isDefined) {
      if (logger.isInfoEnabled) {
        logger.info("Provider already started|path={}", config.toAkkaPath);
      }
      return metadata.get.routerProxy.asInstanceOf[T]
    }

    clazz.synchronized {
      if (metadataMap.contains(config)) {
        logger.info("Provider already started|path={}", config.toAkkaPath)
        return metadataMap.get(config).get.routerProxy.asInstanceOf[T]
      }
      var proxyList:List[T] = List()
      var actorRefList:List[ActorRef]= List()
      var routeePaths:List[String] = List()

      for (i <- 0 to typedActorNum) {
        val proxy: T = typed.typedActorOf(TypedProps[T](clazz, implements))

        val actorRef: ActorRef = typed.getActorRefFor(proxy)
        proxyList = proxy :: proxyList
        actorRefList = actorRef :: actorRefList
        routeePaths = actorRef.path.toStringWithoutAddress :: routeePaths
      }

      val group = new RoundRobinGroup(routeePaths)
      val router = system.actorOf(group.props(),config.toPath)
      val routerProxy = typed.typedActorOf(TypedProps[T](clazz),router)
      val metadata = new ProviderMetadata(config,router,routerProxy)
      metadataMap +=(config -> metadata)
    }

    if(logger.isInfoEnabled){
      logger.info("Provider start successfully|path={}",config.toAkkaPath)
    }
    return metadataMap.get(config).get.routerProxy.asInstanceOf[T]
  }

}

object ProviderContainer{
  def apply(typedActorNum:Int = Constants.TYPED_ACTOR_NUM):ProviderContainer
        = new ProviderContainer(typedActorNum)

}

class ProviderMetadata[T](val config: ProviderConfig, val router: ActorRef, val routerProxy: T) {

  val incresedRoutee: List[Routee] = List()


}
