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


addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")

