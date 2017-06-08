package org.omath.bootstrap

import org.omath._
import org.omath.kernel.Kernel;
import org.omath.kernel.KernelState;
import org.omath.kernel.MutableMapKernelState;
import org.omath.kernel._

class BootstrapKernelState extends MutableMapKernelState {
  import org.omath.symbols.{ Pattern, Blank, BlankNullSequence, String, Null }
  import org.omath.bootstrap.symbols.{ JavaClass, JavaMethod, JavaObject, JavaNew }

  {
    val _String = Blank(String)
    val class_String = Pattern('class, _String)
    val class_JavaClass = Pattern('class, Blank(JavaClass))
    val method_String = Pattern('method, _String)
    val method_JavaMethod = Pattern('method, Blank(JavaMethod))
    val object_JavaObject = Pattern('object, Blank(JavaObject))
    val `arguments:_[___]` = Pattern('arguments, Blank()(BlankNullSequence()))
    val `object:Null` = Pattern('object, Null)
    
    addDownValues(JavaClass, JavaClass(class_String) :> ClassForNameBindable)
    addDownValues(JavaClass, JavaClass(object_JavaObject) :> GetClassBindable)
    addDownValues(JavaMethod, JavaMethod(class_String, method_String) :> JavaMethodBindable)
    addDownValues(JavaMethod, JavaMethod(class_JavaClass, method_String) :> JavaMethodBindable)
    addSubValues(JavaMethod, (method_JavaMethod)(object_JavaObject, `arguments:_[___]`) :> MethodInvocationBindable)
    addSubValues(JavaMethod, (method_JavaMethod)(`object:Null`, `arguments:_[___]`) :> MethodInvocationBindable)
    addDownValues(JavaNew, JavaNew(class_String, `arguments:_[___]`) :> JavaNewBindable)
    addDownValues(JavaNew, JavaNew(class_JavaClass, `arguments:_[___]`) :> JavaNewBindable)

    import org.omath.symbols.{ SetDelayed, HoldAll }

    addAttributes(SetDelayed, HoldAll)

    val _lhs = Pattern('lhs, Blank())
    val _rhs = Pattern('rhs, Blank())

    addDownValues(SetDelayed, SetDelayed(_lhs, _rhs) :> SetDelayedBindable)
  }
}

// a mixin for kernels
trait BootstrapState { kernel: Kernel =>
  override val kernelState = new BootstrapKernelState
}
