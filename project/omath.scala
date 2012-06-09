import sbt._
import Keys._

object Omath extends Build {
    import BuildSettings._
    import Dependencies._

    lazy val root = Project(id = "omath",
                            base = file("."),
			    settings = buildSettings) aggregate(api, tungsten, bootstrap, tungstenBootstrap, core, tungstenCore, parser, ui)

    lazy val api = Project(id = "omath-api",
                           base = file("api"),
                            settings = buildSettings ++ Seq(libraryDependencies += apfloat)) dependsOn()

    lazy val patterns = Project(id = "omath-patterns",
                           base = file("patterns"),
                            settings = buildSettings) dependsOn(api)

    lazy val tungsten = Project(id = "omath-tungsten",
                           base = file("tungsten"),
                            settings = buildSettings ++ Seq(libraryDependencies += toolkit.base)) dependsOn(patterns)

    lazy val bootstrap = Project(id = "omath-bootstrap",
                           base = file("bootstrap"),
                            settings = buildSettings ++ Seq(libraryDependencies += toolkit.base)) dependsOn(api)

    lazy val tungstenBootstrap = Project(id = "omath-tungsten-bootstrap",
                           base = file("tungsten-bootstrap"),
                            settings = buildSettings) dependsOn(tungsten, bootstrap, parser)

    lazy val parser = Project(id = "omath-parser",
                           base = file("parser"),
                            settings = buildSettings) dependsOn(api)

    lazy val core = Project(id = "omath-core",
                           base = file("core"),
                            settings = buildSettings) dependsOn(bootstrap)

    lazy val tungstenCore = Project(id = "omath-tungsten-core",
                           base = file("tungsten-core"),
                            settings = buildSettings) dependsOn(tungstenBootstrap, core)

    lazy val ui = Project(id = "omath-ui",
                           base = file("ui"),
                            settings = buildSettings) dependsOn(tungstenCore)


}

object BuildSettings {
  import Resolvers._
  import Dependencies._

  val buildOrganization = "org.omath"
  val buildVersion      = "0.0.1"
  val buildScalaVersion = "2.10.0-M3"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions += "-feature",
    scalacOptions += "-unchecked",
//    scalacOptions += "-Xprint:typer",
    publishTo    := Some(Resolver.sftp("toolkit.tqft.net Maven repository", "tqft.net", "tqft.net/releases") as ("scottmorrison", new java.io.File("/Users/scott/.ssh/id_rsa"))),
    resolvers    := sonatypeResolvers ++ tqftResolvers,
    libraryDependencies += {
        val (scalatestVersion, scalatestScalaVersion) = buildScalaVersion match {
                case sv if sv.startsWith("2.9") => ("1.8", "2.9.0")
                case sv if sv.startsWith("2.10") => ("1.8-SNAPSHOT", "2.10.0-M3")
        }
        ("org.scalatest" % ("scalatest_" + scalatestScalaVersion) % scalatestVersion % "test" )
    },
    libraryDependencies ++= Seq(junit, slf4j)
  )
}

object Resolvers {
        val sonatypeResolvers = Seq(
                "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
                "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases"
        )
	val tqftResolvers = Seq(
		"tqft.net Maven repository" at "http://tqft.net/releases"	
	)
}

object Dependencies {
	object toolkit {
		val base = "net.tqft" %% "toolkit.base" % "0.1.6"
	}
	val junit = "junit" % "junit" % "4.8" % "test"
	val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.6.1"
        val apfloat = "org.apfloat" % "apfloat" % "1.6.3"               // arbitrary precision integers and floats; much better than BigInt and BigDecimal
}
