package com.stuartizon.validator

import scala.language.experimental.macros

trait Validator[X] {
  def validate(id: String, value: X): ValidationResult[X]

  def validate(value: X): ValidationResult[X] = macro ValidationMacros.validate[X]

  def onlyIf(boolean: Boolean): Validator[X] =
    if (boolean) this else BasicValidators.success
}