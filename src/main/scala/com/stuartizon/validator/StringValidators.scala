package com.stuartizon.validator

import cats.data.Validated._

import scala.util.matching.Regex

trait StringValidators {
  val notEmptyValidator: Validator[String] = new Validator[String] {
    override def validate(id: String, string: String): ValidationResult[String] = string match {
      case value if value.isEmpty => invalidNel(ErrorDescription(id, "Must be non empty"))
      case _ => valid(string)
    }
  }

  def regexValidator(regex: Regex, errorMessage: String): Validator[String] = new Validator[String] {
    override def validate(id: String, string: String): ValidationResult[String] = string match {
      case regex(_*) => valid(string)
      case _ => invalidNel(ErrorDescription(id, errorMessage))
    }
  }

  val numericValidator: Validator[String] = regexValidator( """^([0-9]+)$""".r, "Must be a number")

  val emailValidator: Validator[String] = regexValidator( """^([^@\.]+?(\.[^@\.]+?)*?@[^@`.]+?(\.[^@\.]+?)+?)$""".r, "Must be a valid email address")

  val ukPhoneNumberValidator: Validator[String] = regexValidator( """^(\(?44\)? ?|\(?\+\(?44\)? ?|\(?0)(\d\)? ?){9,10}$""".r, "Must be a valid UK phone number")

  val accountNumberValidator: Validator[String] = regexValidator("""^(\d{8})$""".r, "Must be 8 digits")

  val sortCodeValidator: Validator[String] = regexValidator("""^(\d{6})$""".r, "Must be 6 digits")
}

object StringValidators extends StringValidators