package com.kubbo.proxy.http.scan

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.dispatch.Mapper
import akka.io.IO
import akka.pattern.Patterns
import com.kubbo.demo.EchoService
import com.kubbo.proxy.http.HttpProxy
import com.kubbo.rpc.akka.Reference
import spray.can.Http
import spray.http._
/**
 * Created by zhu on 2014/9/28.
 */

class HttpHandlerActor extends Actor with ActorLogging{
  val ref = Reference.get()
  val echoService = ref.getRef(classOf[EchoService],"test","1.0.0")
  override def receive: Receive = {
    case _:Http.Connected =>sender!Http.Register(self)
    case HttpRequest(HttpMethods.GET,Uri.Path("/hello"),_,_,_)=>{
      sender!HttpResponse(status=200,entity = "world")
    }
    case HttpRequest(HttpMethods.GET,Uri.Path("/sync"),_,_,_)=>{
      sender!HttpResponse(status=200,entity = echoService.syncEcho("sync world"))
    }
    case HttpRequest(HttpMethods.GET,Uri.Path("/async"),_,_,_)=>{
      val future = echoService.asyncEcho("async world");
      val response = future.map(new Mapper[String,HttpResponse] {
        override def apply(parameter: String): HttpResponse = {
          return new HttpResponse(status=200,entity = parameter)
        }
      })(context.dispatcher)
      Patterns.pipe(response,context.dispatcher).to(sender)
    }
    case _:HttpRequest =>sender!HttpResponse(404,"Unknown source")
    case Timedout(HttpRequest(method,uri,_,_,_)) =>sender!HttpResponse(status=500,entity = "the "+method+" request to "+uri+" has time out")
  }
}
class ScanHttpProxy extends HttpProxy{
  override def start(): Unit = {
    implicit  val system = ActorSystem("proxy-http")
    val handler = system.actorOf(Props[HttpHandlerActor],"Http-handler")
    IO(Http)!Http.Bind(handler,interface = "127.0.0.1",port=8080)

  }
}




object ScanHttpProxy {
  def apply():ScanHttpProxy = {
    new ScanHttpProxy()
  }
}


