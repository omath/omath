import sbt._

object PluginDef extends Build {
    override lazy val projects = Seq(root)
    lazy val root = Project("plugins", file(".")) dependsOn( webstartPlugin )
    lazy val webstartPlugin = uri("git://github.com/ritschwumm/xsbt-webstart.git")
}

