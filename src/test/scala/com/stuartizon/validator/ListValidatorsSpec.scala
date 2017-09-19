package com.stuartizon.validator

import org.specs2.mutable.Specification

import scalaz.NonEmptyList
import scalaz.syntax.ToValidationOps

class ListValidatorsSpec extends Specification with ListValidators with ToValidationOps with ValidationMatchers {
  val startsWithJ = new Validator[String] {
    override def validate(id: String, value: String) =
      if (value startsWith "J") value.successNel
      else ErrorDescription(id, s"$value does not start with J").failureNel
  }

  "forAll validator" should {
    "validate a valid list" in {
      val list = List("John", "Jack", "James")
      forAll(startsWithJ).validate(list) must beSuccessful(list)
    }

    "validate an empty list" in {
      val list = Nil
      forAll(startsWithJ).validate(list) must beSuccessful(beEqualTo(list))
    }

    "fail for a list with one invalid element" in {
      val list = List("John", "Jack", "Peter")
      forAll(startsWithJ).validate(list) must beFailing(NonEmptyList(ErrorDescription("list", "Peter does not start with J")))
    }

    "fail for a list with multiple invalid elements" in {
      val list = List("John", "Peter", "Simon")
      forAll(startsWithJ).validate(list) must beFailing(NonEmptyList(ErrorDescription("list", "Peter does not start with J"), ErrorDescription("list", "Simon does not start with J")))
    }
  }

  "contains validator" should {
    "validate a list with at least one valid element" in {
      val list = List("John", "Peter")
      contains(startsWithJ).validate(list) must beSuccessful(list)
    }

    "fail for a list with no valid elements" in {
      val list = List("Peter", "Paul")
      contains(startsWithJ).validate(list) must beFailing(NonEmptyList(ErrorDescription("list", "Peter does not start with J"), ErrorDescription("list", "Paul does not start with J")))
    }

    "fail for an empty list" in {
      val list = Nil
      contains(startsWithJ).validate(list) must beFailing(NonEmptyList(ErrorDescription("list", "Must be non empty")))
    }
  }

  "contains value validator" should {
    "validate a list containing the required element" in {
      val list = List("John", "Peter", "Paul")
      contains("Paul").validate(list) must beSuccessful(list)
    }

    "fail for a list missing the required element" in {
      val list = List("John", "Simon", "James")
      contains("Paul").validate(list) must beFailing(NonEmptyList(ErrorDescription("list", "Must contain Paul")))
    }

    "fail for an empty list" in {
      val list = Nil
      contains("Paul").validate(list) must beFailing(NonEmptyList(ErrorDescription("list", "Must contain Paul")))
    }
  }

  "not Nil validator" should {
    "validate a value" in {
      val list = List(2)
      notNil.validate(list) must beSuccessful(list)
    }

    "fail for a None" in {
      val list = Nil
      notNil.validate(list) must beFailing(NonEmptyList(ErrorDescription("list", "Must be non empty")))
    }
  }
}