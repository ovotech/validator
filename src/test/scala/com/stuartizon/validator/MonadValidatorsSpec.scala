package com.stuartizon.validator

import cats.data.NonEmptyList
import cats.data.Validated._
import org.specs2.mutable.Specification

class MonadValidatorsSpec extends Specification with MonadValidators with ValidationMatchers {
  val startsWithJ: Validator[String] = new Validator[String] {
    override def validate(id: String, value: String): ValidationResult[String] =
      if (value startsWith "J") valid(value)
      else invalidNel(ErrorDescription(id, s"$value does not start with J"))
  }

  "forEach validator" should {
    "validate a valid list" in {
      val list = List("John", "Jack", "James")
      forEach[List, String](startsWithJ).validate(list) must beSuccessful(list)
    }

    "validate an empty list" in {
      val list = Nil
      forEach[List, String](startsWithJ).validate(list) must beSuccessful(beEqualTo(list))
    }

    "fail for a list with one invalid element" in {
      val list = List("John", "Jack", "Peter")
      forEach[List, String](startsWithJ).validate(list) must beFailing(NonEmptyList.one(ErrorDescription("list", "Peter does not start with J")))
    }

    "fail for a list with multiple invalid elements" in {
      val list = List("John", "Peter", "Simon")
      forEach[List, String](startsWithJ).validate(list) must beFailing(NonEmptyList.of(ErrorDescription("list", "Peter does not start with J"), ErrorDescription("list", "Simon does not start with J")))
    }

    "validate a present optional" in {
      val name = Some("John")
      forEach[Option, String](startsWithJ).validate(name) must beSuccessful(name)
    }

    "validate a missing optional" in {
      val name = None
      forEach[Option, String](startsWithJ).validate(name) must beSuccessful(name)
    }

    "fail for an invalid optional" in {
      val name = Some("Peter")
      forEach[Option, String](startsWithJ).validate(name) must beFailing(NonEmptyList.one(ErrorDescription("name", "Peter does not start with J")))
    }
  }

  "contains validator" should {
    "validate a list with at least one valid element" in {
      val list = List("John", "Peter")
      contains[List, String](startsWithJ).validate(list) must beSuccessful(list)
    }

    "fail for a list with no valid elements" in {
      val list = List("Peter", "Paul")
      contains[List, String](startsWithJ).validate(list) must beFailing(NonEmptyList.of(ErrorDescription("list", "Peter does not start with J"), ErrorDescription("list", "Paul does not start with J")))
    }

    "fail for an empty list" in {
      val list = Nil
      contains[List, String](startsWithJ).validate(list) must beFailing(NonEmptyList.one(ErrorDescription("list", "Must be non empty")))
    }

    "validate a present optional" in {
      val name = Some("John")
      contains[Option, String](startsWithJ).validate(name) must beSuccessful(name)
    }

    "fail for an invalid optional" in {
      val name = Some("Peter")
      contains[Option, String](startsWithJ).validate(name) must beFailing(NonEmptyList.one(ErrorDescription("name", "Peter does not start with J")))
    }

    "fail for a missing optional" in {
      val name = None
      contains[Option, String](startsWithJ).validate(name) must beFailing(NonEmptyList.one(ErrorDescription("name", "Must be non empty")))
    }
  }

  "contains value validator" should {
    "validate a list containing the required element" in {
      val list = List("John", "Peter", "Paul")
      contains[List, String]("Paul").validate(list) must beSuccessful(list)
    }

    "fail for a list missing the required element" in {
      val list = List("John", "Simon", "James")
      contains[List, String]("Paul").validate(list) must beFailing(NonEmptyList.one(ErrorDescription("list", "Must contain Paul")))
    }

    "fail for an empty list" in {
      val list = Nil
      contains[List, String]("Paul").validate(list) must beFailing(NonEmptyList.one(ErrorDescription("list", "Must contain Paul")))
    }

    "validate a present optional" in {
      val name = Some("John")
      contains[Option, String]("John").validate(name) must beSuccessful(name)
    }

    "fail for an invalid optional" in {
      val name = Some("Peter")
      contains[Option, String]("John").validate(name) must beFailing(NonEmptyList.one(ErrorDescription("name", "Must contain John")))
    }

    "fail for a missing optional" in {
      val name = None
      contains[Option, String]("Paul").validate(name) must beFailing(NonEmptyList.one(ErrorDescription("name", "Must contain Paul")))
    }
  }

  "nonEmpty validator" should {
    "validate a list" in {
      val list = List(2)
      nonEmpty[List, Int].validate(list) must beSuccessful(list)
    }

    "fail for an empty list" in {
      val list = Nil
      nonEmpty[List, String].validate(list) must beFailing(NonEmptyList.one(ErrorDescription("list", "Must be non empty")))
    }

    "validate a present optional" in {
      val name = Some("")
      nonEmpty[Option, String].validate(name) must beSuccessful(name)
    }

    "fail for a missing optional" in {
      val name = None
      nonEmpty[Option, String].validate(name) must beFailing(NonEmptyList.one(ErrorDescription("name", "Must be non empty")))
    }
  }
}