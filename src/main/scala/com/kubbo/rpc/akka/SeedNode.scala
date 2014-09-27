package com.kubbo.rpc.akka

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
  * Created by zhu on 2014/9/27.
  */
object SeedNode {

   def start(port:Int = 5002):Unit = {
     val system = ActorSystem(Constants.SYSTEM,ConfigFactory.parseString(Constants.SEED_NODE_ROLE)
     .withFallback(ConfigFactory.parseString("akka.remote.netty.tcp.port="+port)
     .withFallback(ConfigFactory.load())))


     println("seed-node started")

     while (true){
       SeedNode.getClass.synchronized{
           SeedNode.getClass.wait()
       }
     }
   }

   def main(args: Array[String]) {
     start()
   }
 }
