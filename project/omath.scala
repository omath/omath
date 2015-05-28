import sbt._
import Keys._

object Omath extends Build {
    import BuildSettings._
    import Dependencies._
    import com.typesafe.sbt.SbtStartScript

    lazy val root = Project(id = "omath",
                           base = file("aggregate"),
			                     settings = buildSettings) aggregate (
                            expressions, 
                            parser, 
                            api, 
                            tungsten, 
                            bootstrap, 
                            tungstenBootstrap, 
                            core, 
                            tungstenCore, 
                            rest, 
                            webstart, 
                            applet, 
                            repl, 
                            contrib)

    lazy val expressions = Project(id = "omath-expressions",
                           base = file("expressions"),
                            settings = buildSettings) dependsOn()

    lazy val api = Project(id = "omath-api",
                           base = file("api"),
                            settings = buildSettings ++ Seq(libraryDependencies += apfloat)) dependsOn(expressions)

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
                            settings = buildSettings ++ Seq(libraryDependencies += scala.parser)) dependsOn(expressions)

    lazy val core = Project(id = "omath-core",
                           base = file("core"),
                            settings = buildSettings ++ Seq(libraryDependencies ++= Seq(commons.codec, toolkit.base, toolkit.algebra))) dependsOn(bootstrap)

    lazy val tungstenCore = Project(id = "omath-tungsten-core",
                           base = file("tungsten-core"),
                            settings = buildSettings) dependsOn(tungstenBootstrap, core)

    lazy val rest = Project(id = "omath-ui-rest",
				base = file("ui/rest"),
				settings = buildSettings ++ heroku ++ Seq(libraryDependencies ++= Seq(/* finagle.core, finagle.http */))) dependsOn(tungstenCore)

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
  val buildVersion      = "0.0.1"
  val buildScalaVersion = "2.11.6"

  val buildSettings = Defaults.defaultSettings ++ Seq (
  	isSnapshot := true,
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions += "-feature",
    scalacOptions += "-unchecked",
    publishTo    := Some(Resolver.sftp("tqft.net Maven repository (publishing)", "tqft.net", "tqft.net/releases") as ("scottmorrison", new java.io.File("/Users/scott/.ssh/id_rsa"))),
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
		"tqft.net Maven repository" at "https://tqft.net/releases"	
	)
}

object Dependencies {
  object toolkit {
		val base = "net.tqft" %% "toolkit-base" % "0.1.18-SNAPSHOT"
		val eval = "net.tqft" %% "toolkit-eval" % "0.1.18-SNAPSHOT"
		val algebra = "net.tqft" %% "toolkit-algebra" % "0.1.18-SNAPSHOT"
	}
  object scala {
    val parser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1"
    val xml = "org.scala-lang.modules" %% "scala-xml" % "1.0.1"
  }
  val junit = "junit" % "junit" % "4.8" % "test"
  val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.6.1"
  val scalatest = "org.scalatest" %% "scalatest" % "2.1.4" % "test"
  val apfloat = "org.apfloat" % "apfloat" % "1.6.3"               // arbitrary precision integers and floats; much better than BigInt and BigDecimal
  val jline = "jline" % "jline" % "1.0"
	object finagle {
		val core = "com.twitter" %% "finagle-core" % "6.14.0"
		val http = "com.twitter" %% "finagle-http" % "6.14.0"
	}
	object commons {
		val codec = "commons-codec" % "commons-codec" % "1.6"
	}
}

