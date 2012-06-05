import sbt._

object Omath extends Build {
    lazy val root = Project(id = "omath",
                            base = file(".")) aggregate(api, kernel, bootstrap, core, parser, ui)

    lazy val api = Project(id = "omath.api",
                           base = file("api")) dependsOn()

    lazy val patterns = Project(id = "omath.patterns",
                           base = file("patterns")) dependsOn(api)

    lazy val kernel = Project(id = "omath.kernel",
                           base = file("kernel")) dependsOn(patterns)

    lazy val bootstrap = Project(id = "omath.bootstrap",
                           base = file("bootstrap")) dependsOn(api)

    lazy val parser = Project(id = "omath.parser",
                           base = file("parser")) dependsOn(api)

    lazy val core = Project(id = "omath.core",
                           base = file("core")) dependsOn(bootstrap, kernel, parser)

    lazy val ui = Project(id = "omath.ui",
                           base = file("ui")) dependsOn(core)


}
