package com.stuartizon.validator

import scalaz.syntax.ToValidationOps

trait BasicValidators extends ToValidationOps {
  def success[X]: Validator[X] = new Validator[X] {
    def validate(id: String, value: X) = value.successNel
  }

  def failure[X](message: String): Validator[X] = new Validator[X] {
    def validate(id: String, value: X) = ErrorDescription(id, message).failureNel
  }
}

object BasicValidators extends BasicValidators