package com.stuartizon.validator

import scalaz.Scalaz._

trait OptionValidators {
  /** Validates the value inside the option if present. Succeeds if this option is a None.
    *
    * @param inner validator to use if the option is present */
  def optional[X](inner: Validator[X]) = new Validator[Option[X]] {
    override def validate(id: String, option: Option[X]) =
      option match {
        case Some(value) => inner.validate(id, value).map(Some(_))
        case None => None.success
      }
  }

  /** Validates that the option is not empty and the value inside validates.
    * Fails if the inner validation fails or the option is a None.
    *
    * @param inner validator to use if the option is present */
  def mandatory[X](inner: Validator[X]) = new Validator[Option[X]] {
    override def validate(id: String, option: Option[X]): ValidationResult[Option[X]] = {
      option.map(inner.validate(id, _).map(Some(_))).getOrElse(ErrorDescription(id, "Must be non empty").failureNel)
    }
  }

  /** Validates that the option is not empty and the value inside matches.
    * Fails if the option does not contain the given value
    *
    * @param value the option must contain this value */
  def mandatory[X](value: X) = new Validator[Option[X]] {
    override def validate(id: String, option: Option[X]) =
      if (option.contains(value)) option.successNel
      else ErrorDescription(id, s"Must contain $value").failureNel
  }

  /** Validates that the option is not empty. Fails is the option is a None. */
  def mandatory[X] = new Validator[Option[X]] {
    override def validate(id: String, option: Option[X]) =
      if (option.isDefined) option.successNel
      else ErrorDescription(id, s"Must be non empty").failureNel
  }
}

object OptionValidators extends OptionValidators