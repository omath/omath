package org.omath.util

sealed trait Result[T] {
  def map[S](f: T => S): Result[S] = flatMap(f andThen (Success.apply _))
  def flatMap[S](f: T => Result[S]): Result[S]
  def get: T
  def getFailureMessage: String
  def isSuccess: Boolean
  def isFailure = !isSuccess
}
case class Success[T](value: T) extends Result[T] {
  override def flatMap[S](f: T => Result[S]) = {
    try {
      f(value)
    } catch {
      case e: Exception => Failure(e.getMessage, Some(e))
    }
  }
  override def get = value
  override def getFailureMessage = throw new NoSuchElementException
  override def isSuccess = true
}
case class Failure[T](message: String, exception: Option[Exception] = None) extends Result[T] {
  override def flatMap[S](f: T => Result[S]) = Failure[S](message, exception)
  override def get = throw exception.getOrElse(new NoSuchElementException(message))
  override def getFailureMessage = message
  override def isSuccess = false
}
