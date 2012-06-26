import sbt._
import Keys._

object Omath extends Build {
    import BuildSettings._
    import Dependencies._
    import com.typesafe.startscript.StartScriptPlugin

    lazy val all = Project(id = "omath",
                            base = file("aggregrate"),
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
				settings = buildSettings ++ bowlerWebapp ++ heroku) dependsOn(tungstenCore)

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
  import com.typesafe.startscript.StartScriptPlugin

  val buildOrganization = "org.omath"
  val buildVersion      = "0.0.1-SNAPSHOT"
  val buildScalaVersion = "2.9.2"
//  val buildScalaVersion = "2.10.0-M3"
  val buildCrossScalaVersions = Seq("2.9.2", "2.10.0-M3")

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    crossScalaVersions := buildCrossScalaVersions,
//    scalacOptions += "-feature",
    scalacOptions += "-unchecked",
//    scalacOptions += "-Xprint:typer",
    publishTo    := Some(Resolver.sftp("toolkit.tqft.net Maven repository", "tqft.net", "tqft.net/releases") as ("scottmorrison", new java.io.File("/Users/scott/.ssh/id_rsa"))),
    resolvers    := sonatypeResolvers ++ tqftResolvers,
    libraryDependencies <+= (scalaVersion) { sv =>
        val (scalatestVersion, scalatestScalaVersion) = sv match {
                case sv if sv.startsWith("2.9.") => ("1.8", "2.9.0")
                case sv if sv.startsWith("2.10.") => ("1.8-SNAPSHOT", "2.10.0-M3")
        }
        "org.scalatest" % ("scalatest_" + scalatestScalaVersion) % scalatestVersion % "test"
    },
    unmanagedSourceDirectories in Compile <+= (baseDirectory, scalaVersion)((bd, sv) => sv match {
      case sv if sv startsWith "2.9." => bd / "src" / "main" / "scala-2.9"
      case sv if sv startsWith "2.10." => bd / "src" / "main" / "scala-2.10"
    }),
    libraryDependencies ++= Seq(junit, slf4j),
    exportJars := true,
    unmanagedResourceDirectories in Compile <+= (baseDirectory) { bd => bd / "src" / "main" / "omath" },
    StartScriptPlugin.stage in Compile := Unit, 			// fake 'stage' task in the aggregate project, per https://github.com/typesafehub/xsbt-start-script-plugin
    mainClass in Compile := Some("org.omath.ui.rest.Web")
  )

  val heroku = StartScriptPlugin.startScriptForClassesSettings
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
		val base = "net.tqft" %% "toolkit-base" % "0.1.7"
		val eval = "net.tqft" %% "toolkit-eval" % "0.1.7"
		val algebra = "net.tqft" %% "toolkit-algebra" % "0.1.7"
	}
	val junit = "junit" % "junit" % "4.8" % "test"
	val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.6.1"
        val apfloat = "org.apfloat" % "apfloat" % "1.6.3"               // arbitrary precision integers and floats; much better than BigInt and BigDecimal
	val bowler = "org.bowlerframework" % "core_2.9.1" % "0.6"
	val jetty = "org.mortbay.jetty" % "jetty" % "6.1.26" % "container, compile"
	val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided"
	val includeBowlerIn29 = libraryDependencies <++= (scalaVersion) {
	      	case sv if sv startsWith "2.9." => Seq(bowler, jetty, servlet)
      		case sv if sv startsWith "2.10." => Seq(jetty, servlet)
	}
	val bowlerWebapp = Seq(includeBowlerIn29) ++ com.github.siasia.WebPlugin.webSettings
	val jline = "jline" % "jline" % "1.0"
	object commons {
		val codec = "commons-codec" % "commons-codec" % "1.6"
	}
}

