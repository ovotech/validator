package com.stuartizon.validator

import cats.data.Validated._

trait NumberValidators {
  def positiveNumberValidator[T](implicit num: Numeric[T]) = new Validator[T] {
    def validate(id: String, number: T): ValidationResult[T] = {
      if (num.gt(number, num.zero)) valid(number)
      else invalidNel(ErrorDescription(id, "Must be a positive number"))
    }
  }
}

object NumberValidators extends NumberValidators