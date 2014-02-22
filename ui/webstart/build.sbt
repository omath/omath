webstartSettings

    webstartGenConfig := Some(GenConfig(
        dname       = "CN=omath.org, OU=omath.org, O=omath.org, L=Toronto, C=CA",
        validity    = 9999
    ))

    webstartKeyConfig := Some(KeyConfig(
        keyStore    = file("keyStore"),
        storePass   = "ambulapcha",
        alias       = "omath",
        keyPass     = "ambulapcha"
    ))

    webstartJnlpConfigs    := Seq(JnlpConfig(
	fileName    = "omath.jnlp",
        descriptor  = (fileName:String, assets:Seq[JnlpAsset]) => {
            <jnlp spec="1.5+" codebase="http://omath.org/webstart/" href={fileName}>
                <information>
                    <title>omath.org</title>
                    <vendor>omath.org</vendor>
                    <description>omath java webstart package</description>
                </information>
                <security>
                    <all-permissions/>
                </security> 
                <resources>
                    <j2se version="1.6+" max-heap-size="192m"/>
                    { assets map { _.toElem } }
                </resources>
                <application-desc main-class="org.omath.ui.rest.WebStart"/>
            </jnlp>
        }
    ))
