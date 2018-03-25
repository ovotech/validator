package com.stuartizon.validator

import java.time.LocalDate

import cats.data.NonEmptyList
import org.specs2.mutable.Specification

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
      atLeast18.validate(dob) must beFailing(NonEmptyList.of(ErrorDescription("dob", ageMessage)))
    }

    "fail for a date of birth just under 18 years" in {
      val dob = LocalDate.now.minusYears(18).plusDays(1)
      atLeast18.validate(dob) must beFailing(NonEmptyList.of(ErrorDescription("dob", ageMessage)))
    }
  }
}