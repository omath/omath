package org.omath.ui.repl

import org.omath.kernel.Tungsten
import org.omath.kernel.MutableState
import org.omath.parser.FullFormParser

object omath extends App {

  object kernel extends Tungsten with MutableState
  
  while(true) {
    println(kernel.evaluate(FullFormParser(readLine).left.get))
  }
}