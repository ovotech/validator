package com.stuartizon.validator

import cats.data.Validated._

trait BasicValidators {
  def success[X]: Validator[X] = new Validator[X] {
    def validate(id: String, value: X): ValidationResult[X] = valid(value)
  }

  def failure[X](message: String): Validator[X] = new Validator[X] {
    def validate(id: String, value: X): ValidationResult[X] = invalidNel(ErrorDescription(id, message))
  }
}

object BasicValidators extends BasicValidators