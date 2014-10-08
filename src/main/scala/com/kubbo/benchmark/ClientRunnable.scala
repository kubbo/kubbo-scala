package com.kubbo.benchmark

/**
 * Created by zhu on 2014/10/3.
 */
trait ClientRunnable extends Runnable{
  def getResult:Map[String,String]
}