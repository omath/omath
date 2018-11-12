lazy val commonSettings = Seq(
  organization := "org.omath",
  version := "0.0.1",
  scalaVersion := "2.12.2",
  // scalaVersion := "2.11.7"
  unmanagedResourceDirectories in Compile += baseDirectory.value / "src" / "main" / "omath",
  // resolvers += Resolver.url("tqft.net", url("https://tqft.net/releases/")),
  resolvers += "tqft.net (releases)" at "https://tqft.net/releases/",
  resolvers += "tqft.net (snapshots)" at "https://tqft.net/snapshots/",
  publishConfiguration := publishConfiguration.value.withOverwrite(true),
  publishTo := Some(Resolver.sftp("tqft.net (publishing)", "tqft.net", "tqft.net/releases") as ("scottmorrison", new java.io.File("/Users/scott/.ssh/id_rsa"))))

val toolkit_base = "net.tqft" %% "toolkit-base" % "0.1.19-SNAPSHOT"
val toolkit_eval = "net.tqft" %% "toolkit-eval" % "0.1.19-SNAPSHOT"
val toolkit_algebra = "net.tqft" %% "toolkit-algebra" % "0.1.19-SNAPSHOT"
val scala_parser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1"
val scala_xml = "org.scala-lang.modules" %% "scala-xml" % "1.0.1"
val junit = "junit" % "junit" % "4.8" % "test"
val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.6.1"
val scalatest = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
val apfloat = "org.apfloat" % "apfloat" % "1.8.3"               // arbitrary precision integers and floats; much better than BigInt and BigDecimal
val jline = "jline" % "jline" % "2.14.3"
val finagle_core = "com.twitter" %% "finagle-core" % "6.41.0"
val finagle_http = "com.twitter" %% "finagle-http" % "6.41.0"
val commons_codec = "commons-codec" % "commons-codec" % "1.6"

lazy val root = (project in file("."))
                  .settings(name := "omath", commonSettings)
                  .aggregate(
                        expressions, 
                        parser, 
                        api, 
                        tungsten, 
                        bootstrap, 
                        tungsten_bootstrap, 
                        core, 
                        tungsten_core, 
                        // rest, 
                        // webstart, 
                        // applet, 
                        // repl, 
                        contrib)

lazy val expressions =
(project in file("expressions"))
   .settings(name := "omath-expressions", commonSettings)

lazy val parser =
(project in file("parser"))
   .settings(name := "omath-parser", commonSettings,
             libraryDependencies += scala_parser)
   .dependsOn(expressions)

lazy val api = 
(project in file("api"))
   .settings(name := "omath-api", commonSettings,
             libraryDependencies += apfloat)
   .dependsOn(expressions)

lazy val patterns = 
(project in file("patterns"))
   .settings(name := "omath-patterns", commonSettings)
   .dependsOn(api)

lazy val tungsten = 
(project in file("tungsten"))
   .settings(name := "omath-tungsten", commonSettings,
             libraryDependencies += toolkit_base)
   .dependsOn(patterns)

lazy val bootstrap = 
(project in file("bootstrap"))
   .settings(name := "omath-bootstrap", commonSettings,
             libraryDependencies += toolkit_base)
   .dependsOn(api)

lazy val tungsten_bootstrap = 
(project in file("tungsten-bootstrap"))
   .settings(name := "omath-tungsten-bootstrap", commonSettings)
   .dependsOn(tungsten, bootstrap, parser)

lazy val core = 
(project in file("core"))
   .settings(name := "omath-core", commonSettings,
             libraryDependencies ++= Seq(commons_codec, toolkit_algebra))
   .dependsOn(bootstrap)

lazy val tungsten_core = 
(project in file("tungsten-core"))
   .settings(name := "omath-tungsten-core", commonSettings)
   .dependsOn(tungsten_bootstrap, core)

lazy val contrib = 
(project in file("contrib"))
   .settings(name := "omath-contrib", commonSettings,
             libraryDependencies ++= Seq(toolkit_base, toolkit_eval))
   .dependsOn(api)

// lazy val rest = Project(id = "omath-ui-rest",
// 		base = file("ui/rest"),
// 		settings = buildSettings ++ heroku ++ Seq(libraryDependencies ++= Seq(/* finagle.core, finagle.http */))) dependsOn(tungstenCore)

// lazy val webstart = Project(id = "omath-ui-webstart",
// 		base = file("ui/webstart"),
// 		settings = buildSettings) dependsOn(rest)

// lazy val applet = Project(id = "omath-ui-applet",
// 		base = file("ui/applet"),
// 		settings = buildSettings) dependsOn(tungstenCore)

// lazy val repl = Project(id = "omath-ui-repl",
// 		base = file("ui/repl"),
// 		settings = buildSettings ++ OneJar.settings ++ Seq(libraryDependencies += jline)) dependsOn(tungstenCore)



// object BuildSettings {
//   import Resolvers._
//   import Dependencies._
//   import com.typesafe.sbt.SbtStartScript

//   val buildOrganization = "org.omath"
//   val buildVersion      = "0.0.1"
//   val buildScalaVersion = "2.12.2"
//   // val buildScalaVersion = "2.11.7"

//   val buildSettings = Defaults.defaultSettings ++ Seq (
//   	isSnapshot := true,
//     organization := buildOrganization,
//     version      := buildVersion,
//     scalaVersion := buildScalaVersion,
//     scalacOptions += "-feature",
//     scalacOptions += "-unchecked",
//     publishTo := {
//         val key = new java.io.File("/Users/scott/.ssh/id_rsa")
//         // if(key.exists) {
//           Some(Resolver.ssh("tqft.net Maven repository (ssh)", "tqft.net", "tqft.net/releases") as ("scottmorrison", key))
//         // } else {
//         //   None
//         // }
//     },
//     resolvers    := sonatypeResolvers ++ tqftResolvers,
//     libraryDependencies ++= Seq(junit, slf4j, scalatest),
//     exportJars := true,
//     unmanagedResourceDirectories in Compile <+= (baseDirectory) { bd => bd / "src" / "main" / "omath" },
//     SbtStartScript.stage in Compile := Unit, 			// fake 'stage' task in the aggregate project, per https://github.com/typesafehub/xsbt-start-script-plugin
//     mainClass in Compile := Some("org.omath.ui.rest.Web")
//   )

//   val heroku = SbtStartScript.startScriptForClassesSettings
// }

// object OneJar {
//     import com.github.retronym.SbtOneJar._
//     val settings = oneJarSettings ++ Seq(exportJars := true, mainClass in oneJar := Some("org.omath.ui.repl.omath"))
// }

// object Resolvers {
//   val sonatypeResolvers = Seq(
//           "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
//           "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases"
//   )
// 	val tqftResolvers = Seq(
// 		"tqft.net Maven repository" at "https://tqft.net/releases"	
// 	)
// }



