package com.kubbo.rpc

/**
 * Created by zhu on 2014/9/24.
 */
trait ProviderLifeCycle {


  def start[T<:AnyRef](clazz:Class[T],implements:T,group:String,version:String):T


//
//  def stop[T](clazz:Class[T],group:String,version:String):Unit
//
//
//  def increaseActor[T](clazz:Class[T],group:String,version:String):T
//
//  def decreaseActor[T](clazz:Class[T],group:String,version:String):T


}
