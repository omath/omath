package org.omath.core.kernel

object Exit {
  def apply() = {
    for(c <- callbacks) c()
  }
  
  private val callbacks = scala.collection.mutable.ListBuffer[Function0[Unit]]()
  
  def registerExitListener(callback: Function0[Unit]) = {
    callbacks += callback
  }
}