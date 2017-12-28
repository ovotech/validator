package com.stuartizon.validator

import cats.data.NonEmptyList
import org.specs2.mutable.Specification

class ValidatorSpec extends Specification with ValidationMatchers {
  "Validator" should {
    "succeed for a successful validation" in {
      val field = "hello"
      BasicValidators.success.validate(field) must beSuccessful("hello")
    }

    "fail with the actual value for a constant failing validation" in {
      BasicValidators.failure("failure message").validate(2) must beFailing(NonEmptyList.one(ErrorDescription("2", "failure message")))
    }

    "fail with the id for a field failing validation" in {
      val field = 1
      BasicValidators.failure("failure message").validate(field) must beFailing(NonEmptyList.one(ErrorDescription("field", "failure message")))
    }

    "fail with the id for a complex field failing validation" in {
      object Object {
        val innerField = 20
      }
      BasicValidators.failure("failure message").validate(Object.innerField) must beFailing(NonEmptyList.one(ErrorDescription("innerField", "failure message")))
    }

    "fail with the id for a function failing validation" in {
      object Object {
        def f(str: String) = 25
      }
      BasicValidators.failure("failure message").validate(Object.f("Hello")) must beFailing(NonEmptyList.one(ErrorDescription( """Object.f("Hello")""", "failure message")))
    }

    "succeed for a false conditional" in {
      BasicValidators.failure("failure message").onlyIf(false).validate("Anything") must beSuccessful("Anything")
    }

    "fail for a true conditional failing validation" in {
      val xyz = "ABC"
      BasicValidators.failure("failure message").onlyIf(true).validate(xyz) must beFailing(NonEmptyList.one(ErrorDescription("xyz", "failure message")))
    }
  }
}