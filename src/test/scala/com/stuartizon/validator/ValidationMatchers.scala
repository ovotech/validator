package com.stuartizon.validator

import cats.data.Validated
import org.specs2.matcher._

import scala.languageFeature.reflectiveCalls

trait ValidationMatchers {
  def beSuccessful[T](t: ValueCheck[T]) = SuccessCheckedMatcher(t)

  def beSuccessful[T] = new SuccessMatcher[T]

  def beFailing[T](t: ValueCheck[T]) = FailureCheckedMatcher(t)

  def beFailing[T] = FailureMatcher[T]()
}

case class SuccessMatcher[T]() extends OptionLikeMatcher[({type l[a] = Validated[_, a]})#l, T, T]("Success", (_: Validated[Any, T]).toEither.right.toOption)

case class SuccessCheckedMatcher[T](check: ValueCheck[T]) extends OptionLikeCheckedMatcher[({type l[a] = Validated[_, a]})#l, T, T]("Success", (_: Validated[Any, T]).toEither.right.toOption, check)

case class FailureMatcher[T]() extends OptionLikeMatcher[({type l[a] = Validated[a, _]})#l, T, T]("Failure", (_: Validated[T, Any]).toEither.left.toOption)

case class FailureCheckedMatcher[T](check: ValueCheck[T]) extends OptionLikeCheckedMatcher[({type l[a] = Validated[a, _]})#l, T, T]("Failure", (_: Validated[T, Any]).toEither.left.toOption, check)