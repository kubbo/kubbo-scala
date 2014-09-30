package com.kubbo.demo

import scala.concurrent.Future

/**
 * Created by zhu on 2014/9/28.
 */
trait EchoService {

  def syncEcho(content:String):String

  def asyncEcho(content:String):Future[String]

}

class EchoServiceImpl extends EchoService{
  override def syncEcho(content: String): String = {
    content
  }

  override def asyncEcho(content: String): Future[String] = {
    Future.successful(content)
  }
}
