package com.stuartizon.validator

import scala.util.matching.Regex
import scalaz.Scalaz._

trait StringValidators {
  val notEmptyValidator = new Validator[String] {
    override def validate(id: String, string: String): ValidationResult[String] = string match {
      case value if value.isEmpty => ErrorDescription(id, "Must be non empty").failureNel
      case _ => string.successNel
    }
  }

  def regexValidator(regex: Regex, errorMessage: String) = new Validator[String] {
    override def validate(id: String, string: String): ValidationResult[String] = string match {
      case regex(_*) => string.successNel
      case _ => ErrorDescription(id, errorMessage).failureNel
    }
  }

  val numericValidator = regexValidator( """^([0-9]+)$""".r, "Must be a number")

  val emailValidator = regexValidator( """^([^@\.]+?(\.[^@\.]+?)*?@[^@`.]+?(\.[^@\.]+?)+?)$""".r, "Must be a valid email address")

  val ukPhoneNumberValidator= regexValidator( """^(\(?44\)? ?|\(?\+\(?44\)? ?|\(?0)(\d\)? ?){9,10}$""".r, "Must be a valid UK phone number")

  val accountNumberValidator= regexValidator("""^(\d{8})$""".r, "Must be 8 digits")

  val sortCodeValidator = regexValidator("""^(\d{6})$""".r, "Must be 6 digits")
}

object StringValidators extends StringValidators