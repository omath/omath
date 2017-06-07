addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "3.0.0")

resolvers += Resolver.url("scalasbt",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

// libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v+"-0.2.11.1"))

// addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.5")

// addSbtPlugin("de.djini" % "xsbt-classpath" % "0.8.0")

// addSbtPlugin("de.djini" % "xsbt-webstart" % "0.15.0")

addSbtPlugin("org.scala-sbt.plugins" % "sbt-onejar" % "0.8")

resolvers += Classpaths.typesafeReleases

addSbtPlugin("com.typesafe.sbt" % "sbt-start-script" % "0.10.0")