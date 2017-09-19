package com.stuartizon.validator

import org.specs2.mutable.Specification

import scalaz.NonEmptyList

class ValidatorSpec extends Specification with ValidationMatchers {
  "Validator" should {
    "succeed for a successful validation" in {
      val field = "hello"
      BasicValidators.success.validate(field) must beSuccessful("hello")
    }

    "fail with the actual value for a constant failing validation" in {
      BasicValidators.failure("failure message").validate(2) must beFailing(NonEmptyList(ErrorDescription("2", "failure message")))
    }

    "fail with the id for a field failing validation" in {
      val field = 1
      BasicValidators.failure("failure message").validate(field) must beFailing(NonEmptyList(ErrorDescription("field", "failure message")))
    }

    "fail with the id for a complex field failing validation" in {
      object Object {
        val innerField = 20
      }
      BasicValidators.failure("failure message").validate(Object.innerField) must beFailing(NonEmptyList(ErrorDescription("innerField", "failure message")))
    }

    "fail with the id for a function failing validation" in {
      object Object {
        def f(str: String) = 25
      }
      BasicValidators.failure("failure message").validate(Object.f("Hello")) must beFailing(NonEmptyList(ErrorDescription( """Object.f("Hello")""", "failure message")))
    }

    "succeed for a false conditional" in {
      BasicValidators.failure("failure message").onlyIf(false).validate("Anything") must beSuccessful("Anything")
    }

    "fail for a true conditional failing validation" in {
      val xyz = "ABC"
      BasicValidators.failure("failure message").onlyIf(true).validate(xyz) must beFailing(NonEmptyList(ErrorDescription("xyz", "failure message")))
    }
  }
}