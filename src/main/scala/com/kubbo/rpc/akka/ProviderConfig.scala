package com.kubbo.rpc.akka

/**
 * Created by zhu on 2014/9/25.
 */
case class ProviderConfig(@transient val clazz:Class[_ <:Any],
                         val implements :Any,
                         g:String,
                         v:String)extends java.io.Serializable{
  val className:String = clazz.getCanonicalName
  val group :String = g
  val version:String = v

  def this(@transient clazz:Class[_<:Any],group:String,version:String) = this(clazz,null,group,version)


  def toPath:String = clazz.getName


  def toAkkaPath:String  = "/user/"+toPath


  override def hashCode(): Int = {
    var result: Int = className.hashCode
    result = 31 * result + group.hashCode
    result = 31 * result + version.hashCode

    return result
  }

  override def toString: String = toPath

  override def equals(o: scala.Any): Boolean = {
    if (this == o) return true
    if (o == null || getClass != o.getClass) return false
    val that: ProviderConfig = o.asInstanceOf[ProviderConfig]

    if (!(className == that.className)) return false
    if (!(group == that.group)) return false


    if (!(version == that.version)) return false




    true
  }
}

