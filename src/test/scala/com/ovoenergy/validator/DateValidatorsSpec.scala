package com.ovoenergy.validator

import org.joda.time.LocalDate
import org.specs2.mutable.Specification

import scalaz.NonEmptyList

class DateValidatorsSpec extends Specification with DateValidators with ValidationMatchers {
  "At least 18 validator" should {
    "validate a date of birth exactly 18 years" in {
      val dob = LocalDate.now.minusYears(18)
      atLeast18.validate(dob) must beSuccessful(dob)
    }

    "validate a date of birth just over 18 years" in {
      val dob = LocalDate.now.minusYears(18).minusDays(1)
      atLeast18.validate(dob) must beSuccessful(dob)
    }

    "fail for a date of birth less than 18 years" in {
      val dob = LocalDate.now
      atLeast18.validate(dob) must beFailing(NonEmptyList(ErrorDescription("dob", "Must be at least 18")))
    }

    "fail for a date of birth just under 18 years" in {
      val dob = LocalDate.now.minusYears(18).plusDays(1)
      atLeast18.validate(dob) must beFailing(NonEmptyList(ErrorDescription("dob", "Must be at least 18")))
    }
  }
}