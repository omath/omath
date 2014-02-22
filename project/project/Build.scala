import sbt._

object PluginDef extends Build {
    override lazy val projects = Seq(root)
    lazy val root = Project("plugins", file(".")) dependsOn( webstartPlugin/*, oneJar */)
    lazy val webstartPlugin = uri("git://github.com/semorrison/xsbt-webstart.git")
    lazy val classPathPlugin = uri("git://github.com/ritschwumm/xsbt-classpath.git")

    // until a version for SBT 0.11.3 is ready
    // lazy val oneJar = uri("git://github.com/sbt/sbt-onejar.git#65a61c14d6bbbee0168fd96a5fc8d2b017eda8b9")
}

