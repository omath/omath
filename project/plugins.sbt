addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.0-RC1")

resolvers += Resolver.url("scalasbt",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v+"-0.2.11.1"))

// addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.5")

addSbtPlugin("de.djini" % "xsbt-webstart" % "0.0.5")

addSbtPlugin("com.github.retronym" % "sbt-onejar" % "0.8-SNAPSHOT")

resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.startscript" % "xsbt-start-script-plugin" % "0.5.2")
