package com.stuartizon.validator
package syntax

import cats.Traverse
import cats.data.Validated._
import cats.syntax.foldable._
import cats.syntax.functor._
import cats.syntax.traverse._

import scala.language.{higherKinds, implicitConversions}

trait TraverseValidatorSyntax {
  implicit def syntaxTraverseValidator[A](validator: Validator[A]): TraverseValidatorOps[A] =
    new TraverseValidatorOps[A](validator)

  def validator[F[_] : Traverse]: TraverseAdhocValidatorOps[F] = new TraverseAdhocValidatorOps[F]
}

final class TraverseValidatorOps[A](validator: Validator[A]) {

  /** Validates that all elements validate against the inner validator.
    * Succeeds if the monad is empty.
    */
  def forEach[F[_] : Traverse]: Validator[F[A]] = (id: String, m: F[A]) =>
    m.traverse[ValidationResult, A](validator.validate(id, _))

  /** Validates that there is at least one element which validates against the inner validator.
    * Fails if there are no such elements.
    */
  def contains[F[_] : Traverse]: Validator[F[A]] = (id: String, m: F[A]) => {
    if (m.isEmpty) invalidNel(ErrorDescription(id, s"Must be non empty"))
    else {
      val v = m.map(validator.validate(id, _))
      if (v.exists(_.isValid)) valid(m)
      else v.sequence[ValidationResult, A]
    }
  }
}

final class TraverseAdhocValidatorOps[F[_] : Traverse] {
  /** Validates that there is at least one element with the given value.
    * Fails if there are no such elements.
    *
    * @param value value which the list must contain */
  def contains[A](value: A): Validator[F[A]] = (id: String, m: F[A]) =>
    if (m.exists(_ == value)) valid(m)
    else invalidNel(ErrorDescription(id, s"Must contain $value"))

  /** Validates that there is at least one element. Fails if there are no elements. */
  def nonEmpty[A]: Validator[F[A]] = (id: String, m: F[A]) =>
    if (m.nonEmpty) valid(m)
    else invalidNel(ErrorDescription(id, s"Must be non empty"))
}
