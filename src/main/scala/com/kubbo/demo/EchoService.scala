package com.kubbo.demo

import scala.concurrent.Future

/**
 * Created by zhu on 2014/9/28.
 */
trait EchoService {

  def syncEcho(content:String,sleep:Long=0,verbose:Boolean=false):String

  def asyncEcho(content:String,sleep:Long=0,verbose:Boolean=false):Future[String]

  def voidEcho(content:String,sleep:Long=0,verbose:Boolean=false)
}

class EchoServiceImpl() extends EchoService{

  override def syncEcho(content: String,sleep:Long=0,verbose:Boolean=false): String = {

    if(verbose){
      println(Thread.currentThread().getName+" execute")
    }
    if(sleep>0){
      Thread.sleep(sleep)
    }

    content
  }

  override def asyncEcho(content: String,sleep:Long=0,verbose:Boolean=false): Future[String] = {
    if(verbose){
      println(Thread.currentThread().getName+" execute")
    }
    if (sleep > 0) {
      Thread.sleep(sleep)
    }

    Future.successful(content)
  }

  override def voidEcho(content: String,sleep:Long=0,verbose:Boolean=false): Unit = {

  }
}
