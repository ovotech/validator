package com.stuartizon.validator

import org.specs2.mutable.Specification

class BasicValidatorsSpec extends Specification with BasicValidators with ValidationMatchers {

  "must validator" should {

    "succeed if the condition is true" in {
      must[Unit](_ => true, "error message").validate(()) must beSuccessful
    }

    "fail if the condition is false" in {
      must[Unit](_ => false, "error message").validate(()) must beFailing
    }

  }

  "mustNot validator" should {

    "fail if the condition is true" in {
      mustNot[Unit](_ => true, "error message").validate(()) must beFailing
    }

    "succeed if the condition is false" in {
      mustNot[Unit](_ => false, "error message").validate(()) must beSuccessful
    }

  }

}
