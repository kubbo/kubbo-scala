name := "kubbo-scala"

version := "1.0"


libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.3.6"

libraryDependencies += "com.typesafe.akka" % "akka-remote_2.10" % "2.3.6"

libraryDependencies += "com.typesafe.akka" % "akka-cluster_2.10" % "2.3.6"


libraryDependencies += "io.spray" % "spray-can" % "1.3.1"

libraryDependencies += "io.spray" % "spray-routing" % "1.3.1"

libraryDependencies += "io.spray" % "spray-caching" % "1.3.1"

libraryDependencies += "io.spray" % "spray-client" % "1.3.1"

libraryDependencies += "io.spray" % "spray-json_2.10" % "1.2.6"

libraryDependencies += "io.spray" % "spray-testkit" % "1.3.1"

libraryDependencies += "com.typesafe.akka" % "akka-testkit_2.10" % "2.3.6"


libraryDependencies += "com.typesafe.akka" % "akka-osgi_2.10" % "2.3.6"

libraryDependencies += "com.typesafe.akka" % "akka-contrib_2.10" % "2.3.6"

libraryDependencies += "com.typesafe.akka" % "akka-camel_2.10" % "2.3.6"

libraryDependencies += "com.typesafe.akka" % "akka-multi-node-testkit_2.10" % "2.3.6"

libraryDependencies += "com.typesafe.akka" % "akka-kernel_2.10" % "2.3.6"

libraryDependencies += "com.typesafe.akka" % "akka-transactor_2.10" % "2.3.6"

libraryDependencies += "org.scala-lang.modules" % "scala-async_2.10" % "0.9.2"

libraryDependencies += "io.netty" % "netty-example" % "5.0.0.Alpha1"

libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.0.2"

libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.0.2"

libraryDependencies += "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.0.2"

libraryDependencies += "com.lmax" % "disruptor" % "3.3.0"

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")



