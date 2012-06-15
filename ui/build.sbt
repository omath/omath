// scalaVersion := "2.9.1"

seq(WebStartPlugin.allSettings:_*)

    webstartGenConf := GenConf(
        dname       = "CN=omath.org, OU=omath.org, O=omath.org, L=Toronto, C=CA",
        validity    = 9999
    )

    webstartKeyConf := KeyConf(
        keyStore    = file("keyStore"),
        storePass   = "ambulapcha",
        alias       = "omath",
        keyPass     = "ambulapcha"
    )

    webstartJnlpConf    := Seq(JnlpConf(
        mainClass       = "org.omath.ui.repl.omath",
        fileName        = "omath.jnlp",
        codeBase        = "http://omath.org/webstart/",
        title           = "omath.org",
        vendor          = "omath.org",
        description     = "omath java webstart package",
	iconName	= None,
	splashName	= None,
        offlineAllowed  = true,
        allPermissions  = true,
        j2seVersion     = "1.6+",
        maxHeapSize     = 192
    ))
