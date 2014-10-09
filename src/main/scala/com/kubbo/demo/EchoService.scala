package com.kubbo.demo

import scala.concurrent.Future

/**
 * Created by zhu on 2014/9/28.
 */
trait EchoService {

  def syncEcho(content:String):String

  def asyncEcho(content:String):Future[String]

  def voidEcho(content:String)
}

class EchoServiceImpl(val sleep:Long = 0,val verbose:Boolean = false) extends EchoService{

  override def syncEcho(content: String): String = {

    if(verbose){
      println(Thread.currentThread().getName+" execute")
    }
    if(sleep>0){
      Thread.sleep(sleep)
    }

    content
  }

  override def asyncEcho(content: String): Future[String] = {
    if(verbose){
      println(Thread.currentThread().getName+" execute")
    }
    if (sleep > 0) {
      Thread.sleep(sleep)
    }

    Future.successful(content)
  }

  override def voidEcho(content: String): Unit = {

  }
}
