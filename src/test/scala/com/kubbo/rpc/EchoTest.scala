package com.kubbo.rpc

import scala.concurrent.Future

/**
 * Created by zhu on 2014/9/28.
 */
trait EchoTest {

  def syncEcho(content:String):String

  def asyncEcho(content:String):Future[String]

}

class EchoTestImpl extends EchoTest{
  override def syncEcho(content: String): String = {
    content
  }

  override def asyncEcho(content: String): Future[String] = {
    Future.successful(content)
  }
}
