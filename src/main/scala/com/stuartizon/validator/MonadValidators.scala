package com.stuartizon.validator

import cats.data.Validated._
import cats.implicits._
import cats.instances.AllInstances
import cats.{Functor, Traverse}

import scala.language.higherKinds

trait MonadValidators extends AllInstances {
  /** Validates that all elements validate against the inner validator.
    * Succeeds if the monad is empty.
    *
    * @param inner validator to use for each element */
  def forEach[F[_] : Traverse, X](inner: Validator[X]): Validator[F[X]] = new Validator[F[X]] {
    override def validate(id: String, m: F[X]): ValidationResult[F[X]] =
      m.traverse[ValidationResult, X](inner.validate(id, _))
  }

  /** Validates that there is at least one element which validates against the inner validator.
    * Fails if there are no such elements.
    *
    * @param inner validator to use for each element */
  def contains[F[_], X](inner: Validator[X])(implicit f: Traverse[F] with Functor[F]): Validator[F[X]] = new Validator[F[X]] {
    override def validate(id: String, m: F[X]): ValidationResult[F[X]] = {
      if (m.isEmpty) invalidNel(ErrorDescription(id, s"Must be non empty"))
      else {
        val v = m.map(inner.validate(id, _))
        if (v.exists(_.isValid)) valid(m)
        else v.sequence[ValidationResult, X]
      }
    }
  }

  /** Validates that there is at least one element with the given value.
    * Fails if there are no such elements.
    *
    * @param value value which the list must contain */
  def contains[F[_] : Traverse, X](value: X): Validator[F[X]] = new Validator[F[X]] {
    override def validate(id: String, m: F[X]): ValidationResult[F[X]] =
      if (m.exists(_ == value)) valid(m)
      else invalidNel(ErrorDescription(id, s"Must contain $value"))
  }

  /** Validates that there is at least one element. Fails if there are no elements. */
  def nonEmpty[F[_] : Traverse, X]: Validator[F[X]] = new Validator[F[X]] {
    override def validate(id: String, m: F[X]): ValidationResult[F[X]] =
      if (m.nonEmpty) valid(m)
      else invalidNel(ErrorDescription(id, s"Must be non empty"))
  }
}