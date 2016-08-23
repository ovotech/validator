package com.ovoenergy.validator

import scalaz.Scalaz._

trait ListValidators {
  /** Validates that all elements in the list validate against the inner validator.
    * Succeeds if the list is empty.
    *
    * @param inner validator to use for each element of the list */
  def forAll[X](inner: Validator[X]) = new Validator[List[X]] {
    override def validate(id: String, list: List[X]) = {
      list.foldRight[ValidationResult[List[X]]](Nil.successNel) {
        case (x, acc) => (inner.validate(id, x) |@| acc) (_ :: _)
      }
    }
  }

  /** Validates that the list contains at least one element which validates against the inner validator.
    * Fails with the given message if there are no such elements.
    *
    * @param inner validator to use for each element of the list */
  def contains[X](inner: Validator[X]) = new Validator[List[X]] {
    override def validate(id: String, list: List[X]) = {
      if (list.isEmpty) ErrorDescription(id, s"Must be non empty").failureNel
      else {
        val v = list.map(inner.validate(id, _))
        if (v.exists(_.isSuccess)) list.successNel
        else v.sequence[({type l[a] = ValidationResult[a]})#l, X]
      }
    }
  }

  /** Validates that the list contains the given value at least once.
    * Fails if the list does not contain the given value.
    *
    * @param value value which the list must contain */
  def contains[X](value: X) = new Validator[List[X]] {
    override def validate(id: String, list: List[X]) =
      if (list.contains(value)) list.successNel
      else ErrorDescription(id, s"Must contain $value").failureNel
  }

  /** Validates that the list is not empty. Fails if the list is a Nil. */
  def notNil[X] = new Validator[List[X]] {
    override def validate(id: String, list: List[X]) =
      if (list.nonEmpty) list.successNel
      else ErrorDescription(id, s"Must be non empty").failureNel
  }
}

object ListValidators extends ListValidators