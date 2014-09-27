package com.kubbo.rpc


trait Ref {

  def getRef[T<:AnyRef](clazz:Class[T],group:String,version:String):T
}
