package com.kubbo.rpc.akka

import scala.concurrent.ExecutionContext

/**
 * Created by zhu on 2014/9/27.
 */
object Context {


  def context:ExecutionContext = {
    Reference.context()
  }
}
