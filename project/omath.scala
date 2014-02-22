import sbt._
import Keys._

object Omath extends Build {
    import BuildSettings._
    import Dependencies._
    import com.typesafe.sbt.SbtStartScript

    lazy val all = Project(id = "omath",
                            base = file("aggregate"),
			    settings = buildSettings) aggregate(api, tungsten, bootstrap, tungstenBootstrap, core, tungstenCore, parser, rest, webstart, applet, repl, contrib)

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
                            settings = buildSettings ++ Seq(libraryDependencies ++= Seq(commons.codec, toolkit.base, toolkit.algebra))) dependsOn(bootstrap)

    lazy val tungstenCore = Project(id = "omath-tungsten-core",
                           base = file("tungsten-core"),
                            settings = buildSettings) dependsOn(tungstenBootstrap, core)

    lazy val rest = Project(id = "omath-ui-rest",
				base = file("ui/rest"),
				settings = buildSettings ++ heroku ++ Seq(libraryDependencies ++= Seq(finagle.core, finagle.http))) dependsOn(tungstenCore)

    lazy val webstart = Project(id = "omath-ui-webstart",
				base = file("ui/webstart"),
				settings = buildSettings) dependsOn(rest)

    lazy val applet = Project(id = "omath-ui-applet",
				base = file("ui/applet"),
				settings = buildSettings) dependsOn(tungstenCore)

    lazy val repl = Project(id = "omath-ui-repl",
				base = file("ui/repl"),
				settings = buildSettings ++ OneJar.settings ++ Seq(libraryDependencies += jline)) dependsOn(tungstenCore)

    lazy val contrib = Project(id = "omath-contrib",
				base = file("contrib"),
				settings = buildSettings ++ Seq(libraryDependencies ++= Seq(toolkit.base, toolkit.eval))) dependsOn(api)
}

object BuildSettings {
  import Resolvers._
  import Dependencies._
  import com.typesafe.sbt.SbtStartScript

  val buildOrganization = "org.omath"
  val buildVersion      = "0.0.1-SNAPSHOT"
  val buildScalaVersion = "2.10.3"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions += "-feature",
    scalacOptions += "-unchecked",
    publishTo    := Some(Resolver.sftp("toolkit.tqft.net Maven repository", "tqft.net", "tqft.net/releases") as ("scottmorrison", new java.io.File("/Users/scott/.ssh/id_rsa"))),
    resolvers    := sonatypeResolvers ++ tqftResolvers,
    libraryDependencies ++= Seq(junit, slf4j, scalatest),
    exportJars := true,
    unmanagedResourceDirectories in Compile <+= (baseDirectory) { bd => bd / "src" / "main" / "omath" },
    SbtStartScript.stage in Compile := Unit, 			// fake 'stage' task in the aggregate project, per https://github.com/typesafehub/xsbt-start-script-plugin
    mainClass in Compile := Some("org.omath.ui.rest.Web")
  )

  val heroku = SbtStartScript.startScriptForClassesSettings
}

object OneJar {
    import com.github.retronym.SbtOneJar._
    val settings = oneJarSettings ++ Seq(exportJars := true, mainClass in oneJar := Some("org.omath.ui.repl.omath"))
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
		val base = "net.tqft" %% "toolkit-base" % "0.1.17-SNAPSHOT"
		val eval = "net.tqft" %% "toolkit-eval" % "0.1.17-SNAPSHOT"
		val algebra = "net.tqft" %% "toolkit-algebra" % "0.1.17-SNAPSHOT"
	}
	val junit = "junit" % "junit" % "4.8" % "test"
	val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.6.1"
	val scalatest = "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
        val apfloat = "org.apfloat" % "apfloat" % "1.6.3"               // arbitrary precision integers and floats; much better than BigInt and BigDecimal
	val jline = "jline" % "jline" % "1.0"
	object finagle {
		val core = "com.twitter" %% "finagle-core" % "6.1.0"
		val http = "com.twitter" %% "finagle-http" % "6.1.0"
	}
	object commons {
		val codec = "commons-codec" % "commons-codec" % "1.6"
	}
}

