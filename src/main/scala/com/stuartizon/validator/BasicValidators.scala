package com.stuartizon.validator

import cats.data.Validated._

trait BasicValidators {
  def success[X]: Validator[X] = new Validator[X] {
    def validate(id: String, value: X): ValidationResult[X] = valid(value)
  }

  def failure[X](message: String): Validator[X] = new Validator[X] {
    def validate(id: String, value: X): ValidationResult[X] = invalidNel(ErrorDescription(id, message))
  }

  def must[X](condition: X => Boolean, message: String) = new Validator[X] {
    def validate(id: String, value: X): ValidationResult[X] =
      if (condition(value)) {
        valid(value)
      } else {
        invalidNel(ErrorDescription(id, message))
      }
  }

  def mustNot[X](condition: X => Boolean, message: String) =
    must[X](!condition(_), message)
}

object BasicValidators extends BasicValidators
