package org.omath.bootstrap

import org.omath._
import org.omath.kernel.Kernel
import java.lang.reflect.Method
import org.omath.bootstrap.conversions.Converter
import org.omath.kernel.Evaluation
import java.lang.reflect.Type
import net.tqft.toolkit.Logging
import scala.collection.mutable.ListBuffer
import java.net.URLClassLoader
import java.net.URL

case object ClassLoaders {
  private val primaryClassLoader = getClass.getClassLoader
  private val loaders = scala.collection.mutable.ListBuffer[ClassLoader](primaryClassLoader)
  def registerClassLoader(classLoader: ClassLoader) {
    loaders += classLoader
  }
  def registerURL(url: String) { 
    registerClassLoader(new URLClassLoader(Array[URL](new URL(url)), primaryClassLoader))
  }
  def lookupClass(name: String): Option[Class[_]] = {
    loaders.view.map(cl => try { Some(cl.loadClass(name)) } catch { case _ => None }).find(_.nonEmpty).map(_.get)
  }
}

case object ClassForNameBindable extends PassiveBindable {
  override def bind(binding: Map[SymbolExpression, Expression]): JavaClassExpression = {
    binding('class) match {
      case StringExpression(name) => ClassLoaders.lookupClass(name) match {
        case Some(clazz) => JavaClassExpression(clazz)
        case None => throw new ClassNotFoundException(name)
      }
    }
  }
}

case object GetClassBindable extends PassiveBindable {
  override def bind(binding: Map[SymbolExpression, Expression]): JavaClassExpression = {
    val clazz: Class[_] = binding('object) match {
      case obj: JavaObjectExpression[_] => obj.contents.getClass
    }

    JavaClassExpression(clazz)
  }
}

// FIXME it sure would be nice to be able to invoke overloaded methods...
case object JavaMethodBindable extends PassiveBindable {
  override def bind(binding: Map[SymbolExpression, Expression]): JavaMethodExpression = {
    val clazz: Class[_] = binding('class) match {
      case StringExpression(name) => ClassLoaders.lookupClass(name).getOrElse(throw new ClassNotFoundException(name))
      case javaObject: JavaObjectExpression[_] => javaObject.contents match {
        case clazz: Class[_] => clazz
      }
    }

    val StringExpression(methodName) = binding('method)

    JavaMethodExpression(clazz.getMethods.find(_.getName == methodName).get)
  }
}

trait Boxing extends Logging {
  def box(arguments: Seq[Expression], types: Seq[Type])(implicit evaluation: Evaluation): Option[Seq[Object]] = {
    if (arguments.size > types.size) {
      None
    } else if (arguments.size < types.size) {
      // try to provide some arguments 'implicitly'
      types.last.toString match {
        case "interface org.omath.kernel.Evaluation" => {
          info("providing an implicit evaluation instance while boxing arguments")
          box(arguments, types.dropRight(1)).map(_ :+ evaluation)
        }
        case "interface org.omath.kernel.Kernel" => {
          //          if(types.last.asInstanceOf[Class[_]] != evaluation.kernel.getClass) {
          //            val thatKernel = types.last.asInstanceOf[Class[_]]
          //            val thisKernel = evaluation.kernel.getClass
          //            if(!thatKernel.isAssignableFrom(thisKernel)) {
          //              def up(c: Class[_]): List[Class[_]] = {
          //                if(c == null) {
          //                  Nil
          //                } else {
          //                  c :: c.getInterfaces.toList.flatMap(i => up(i.asInstanceOf[Class[Any]])).toList ::: up(c.getSuperclass)
          //                }
          //              }
          //             val stuff = up(thisKernel).toIndexedSeq
          //             val thisKernel2 = stuff.find(_.getName == "org.omath.kernel.Kernel").get
          //            
          //             println(thatKernel)
          //             println(thisKernel2)
          //             println(thatKernel.getClassLoader.getClass)
          //             println(thisKernel2.getClassLoader.getClass)
          //             
          //              require(false)
          //            }
          //          }
          info("providing an implicit kernel instance while boxing arguments")
          box(arguments, types.dropRight(1)).map(_ :+ evaluation.kernel)
        }
        case _ => None
      }
    } else {
      val options: Seq[Option[Object]] = arguments.zip(types).map({ p => Converter.fromExpression(p._1, p._2) })
      if (options.exists(_.isEmpty)) {
        None
      } else {
        Some(options.map(_.get))
      }
    }
  }
  def unbox(obj: Object): Expression = {
    Converter.toExpression(obj)
  }
}

case object MethodInvocationBindable extends Bindable with Boxing {
  override def activeBind(binding: Map[SymbolExpression, Expression])(implicit evaluation: Evaluation): Expression = {
    val method = binding('method).asInstanceOf[JavaMethodExpression].contents
    val o = binding('object) match {
      case org.omath.symbols.Null => null
      case j: JavaObjectExpression[_] => j.contents
    }
    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments

    box(arguments, method.getGenericParameterTypes)(evaluation) match {
      case Some(boxedArguments) => unbox(try {
        method.invoke(o, boxedArguments: _*)
      } catch {
        case e: IllegalAccessException => 
          // try harder this time!
          info("IllegalAccessException during method invocation; hacking...")
          method.setAccessible(true)
          method.invoke(o, boxedArguments: _*)
      })
      case None => {
        // TODO report that the arguments couldn't be coerced via the evaluation instance?
        System.err.println("The arguments " + arguments + " could not be coerced to match the signature of " + method + ": ")
        System.err.println(method.getGenericParameterTypes.map(_.toString).mkString("{", ", ", "}"))
        org.omath.symbols.$Failed
      }
    }

  }
}

case object JavaNewBindable extends Bindable with Boxing {
  override def activeBind(binding: Map[SymbolExpression, Expression])(implicit evaluation: Evaluation): Expression = {
    val clazz: Class[_] = binding('class) match {
      case StringExpression(name) => ClassLoaders.lookupClass(name).getOrElse(throw new ClassNotFoundException(name))
      case javaObject: JavaObjectExpression[_] => javaObject.contents match {
        case clazz: Class[_] => clazz
      }
    }

    val arguments = binding('arguments).asInstanceOf[FullFormExpression].arguments

    (for (
      constructor <- clazz.getConstructors.view;
      boxedArguments <- box(arguments, constructor.getGenericParameterTypes)(evaluation)
    ) yield unbox(constructor.newInstance(boxedArguments: _*).asInstanceOf[Object])).headOption match {
      case Some(result) => result
      case None => {
        // TODO report that no constructor was found via the evaluation instance?
        System.err.println("No constructor found on " + clazz + " suitable for arguments " + arguments + ", available were: ")
        for (constructor <- clazz.getConstructors) System.err.println(constructor.getGenericParameterTypes.map(_.toString).mkString("{", ", ", "}"))
        org.omath.symbols.$Failed
      }

    }

  }
}

case object SetDelayedBindable extends Bindable {
  private object SubValueAttachesTo {
    @scala.annotation.tailrec
    final def unapply(x: FullFormExpression): Option[SymbolExpression] = {
      import org.omath.symbols.{ Pattern, Blank }
      x.head match {
        case s: SymbolExpression => None
        case Pattern(_, Blank(s: SymbolExpression)) => Some(s)
        case FullFormExpression(s: SymbolExpression, _) => Some(s)
        case h: FullFormExpression => unapply(h)
      }
    }
  }

  override def activeBind(binding: Map[SymbolExpression, Expression])(implicit evaluation: Evaluation): SymbolExpression = {
    val lhs = binding('lhs)
    val rhs = binding('rhs)
    val state = evaluation.kernel.kernelState
    lhs match {
      case lhs: SymbolExpression => state.addOwnValues(lhs, lhs :> rhs)
      case lhs @ FullFormExpression(s: SymbolExpression, _) => state.addDownValues(s, lhs :> rhs)
      case SubValueAttachesTo(s) => state.addSubValues(s, lhs :> rhs)
      case lhs: FullFormExpression => state.addSubValues(lhs.symbolHead, lhs :> rhs)
    }

    org.omath.symbols.Null
  }
}