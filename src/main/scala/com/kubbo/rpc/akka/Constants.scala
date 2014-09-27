package com.kubbo.rpc.akka

/**
 * Created by zhu on 2014/9/24.
 */
object  Constants {


  val CPU_CORE = Runtime.getRuntime.availableProcessors()

  val SYSTEM = "kubbo"


  val CONFIG_PROVIDER_ROLE = "akka.cluster.roles=[provider]"

  val CONFIG_CONSUMER_ROLE = "akka.cluster.roles=[consumer]"

  val PROVIDER_ROLE = "provider"

  val CONSUMER_ROLE = "consumer"
  

  
  val SEED_NODE_ROLE = "akka.cluster.roles=[seed-node]"

  val TYPED_ACTOR_NUM = CPU_CORE*2


  val PROVIDER_TOTAL_INSTANCE = 64







}
