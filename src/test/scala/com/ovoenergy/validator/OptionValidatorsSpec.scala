package com.ovoenergy.validator

import org.specs2.mutable.Specification

import scalaz.NonEmptyList

class OptionValidatorsSpec extends Specification with OptionValidators with StringValidators with ValidationMatchers {
  "optional validator" should {
    "validate a valid value" in {
      val name = Some("John")
      optional(notEmptyValidator).validate(name) must beSuccessful(name)
    }

    "validate a None" in {
      val name = None
      optional(notEmptyValidator).validate(name) must beSuccessful(name)
    }

    "fail for an invalid value" in {
      val name = Some("")
      optional(notEmptyValidator).validate(name) must beFailing(NonEmptyList(ErrorDescription("name", "Must be non empty")))
    }
  }

  "mandatory validator" should {
    "validate a valid value" in {
      val number = Some("123456")
      mandatory(numericValidator).validate(number) must beSuccessful(number)
    }

    "fail for an invalid value" in {
      val number = Some("John")
      mandatory(numericValidator).validate(number) must beFailing(NonEmptyList(ErrorDescription("number", "Must be a number")))
    }

    "fail for a None" in {
      val number = None
      mandatory(numericValidator).validate(number) must beFailing(NonEmptyList(ErrorDescription("number", "Must be non empty")))
    }
  }

  "mandatory value validator" should {
    "validate a valid value" in {
      val name = Some("John")
      mandatory("John").validate(name) must beSuccessful(name)
    }

    "fail for an invalid value" in {
      val name = Some("Peter")
      mandatory("John").validate(name) must beFailing(NonEmptyList(ErrorDescription("name", "Must contain John")))
    }

    "fail for a None" in {
      val name = None
      mandatory("Paul").validate(name) must beFailing(NonEmptyList(ErrorDescription("name", "Must contain Paul")))
    }
  }

  "mandatory option validator" should {
    "validate a value" in {
      val name = Some("")
      mandatory.validate(name) must beSuccessful(name)
    }

    "fail for a None" in {
      val name = None
      mandatory.validate(name) must beFailing(NonEmptyList(ErrorDescription("name", "Must be non empty")))
    }
  }
}