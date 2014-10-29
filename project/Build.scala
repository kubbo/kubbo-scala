import sbt._
import sbt.Keys._
import xerial.sbt.Pack._

object Build extends sbt.Build {

  lazy val root = Project(
    id = "kubbo-scala",
    base = file("."),
    settings = Defaults.defaultSettings
      ++ packAutoSettings // This settings add pack and pack-archive commands to sbt
      ++ Seq(
      // [Optional] If you used packSettings instead of packAutoSettings,
      //  specify mappings from program name -> Main class (full package path)
      // packMain := Map("hello" -> "myprog.Hello"),
      // Add custom settings here
      // [Optional] JVM options of scripts (program name -> Seq(JVM option, ...))
      packJvmOpts := Map("kubbo-scala" -> Seq("-Xmx512m")),
      // [Optional] Extra class paths to look when launching a program. You can use ${PROG_HOME} to specify the base directory
//      packExtraClasspath := Map("hello" -> Seq("${PROG_HOME}/etc")),
      // [Optional] (Generate .bat files for Windows. The default value is true)
      packGenerateWindowsBatFile := true,
        // [Optional] jar file name format in pack/lib folder (Since 0.5.0)
        //   "default"   (project name)-(version).jar
        //   "full"      (organization name).(project name)-(version).jar
        //   "no-version" (organization name).(project name).jar
        //   "original"  (Preserve original jar file names)
        packJarNameConvention := "default",
  // [Optional] List full class paths in the launch scripts (default is false) (since 0.5.1)
  packExpandedClasspath := false,
  // [Optional] Resource directory mapping to be copied within target/pack. Default is Map("{projectRoot}/src/pack" -> "")
//      packResourceDir += (baseDirectory.value / "src/resources" -> "conf")


      packResourceDir += (baseDirectory.value / "src/main/resources" -> "conf"),
        crossPaths := false
  )
  // To publish tar.gz archive to the repository, add the following line (since 0.3.6)
  // ++ publishPackArchive
  // Before 0.3.6, use below:
  // ++ addArtifact(Artifact("myprog", "arch", "tar.gz"), packArchive).settings
  )
}