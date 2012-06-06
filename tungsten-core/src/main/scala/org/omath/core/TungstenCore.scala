package org.omath.core

import org.omath.kernel.tungsten.Tungsten
import org.omath.bootstrap.TungstenBootstrap

trait TungstenCore extends TungstenBootstrap with Core
object TungstenCore extends TungstenCore