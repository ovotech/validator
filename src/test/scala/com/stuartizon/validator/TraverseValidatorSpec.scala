package com.stuartizon.validator

import cats.data.NonEmptyList
import cats.data.Validated._
import cats.instances.all._
import com.stuartizon.validator.syntax.traverseValidator._
import org.specs2.mutable.Specification

class TraverseValidatorSpec extends Specification with ValidationMatchers {
  val startsWithJ: Validator[String] = new Validator[String] {
    override def validate(id: String, value: String): ValidationResult[String] =
      if (value startsWith "J") valid(value)
      else invalidNel(ErrorDescription(id, s"$value does not start with J"))
  }

  "forEach validator" should {

    "validate a valid list" in {
      val list = List("John", "Jack", "James")
      startsWithJ.forEach[List].validate(list) must beSuccessful(list)
    }

    "validate an empty list" in {
      val list: List[String] = Nil
      startsWithJ.forEach[List].validate(list) must beSuccessful(list)
    }

    "fail for a list with one invalid element" in {
      val list = List("John", "Jack", "Peter")
      startsWithJ.forEach[List].validate(list) must beFailing(NonEmptyList.of(ErrorDescription("list", "Peter does not start with J")))
    }

    "fail for a list with multiple invalid elements" in {
      val list = List("John", "Peter", "Simon")
      startsWithJ.forEach[List].validate(list) must beFailing(NonEmptyList.of(ErrorDescription("list", "Peter does not start with J"), ErrorDescription("list", "Simon does not start with J")))
    }

    "validate a present optional" in {
      val name = Some("John")
      startsWithJ.forEach[Option].validate(name) must beSuccessful(name)
    }

    "validate a missing optional" in {
      val name = None
      startsWithJ.forEach[Option].validate(name) must beSuccessful(name)
    }

    "fail for an invalid optional" in {
      val name = Some("Peter")
      startsWithJ.forEach[Option].validate(name) must beFailing(NonEmptyList.of(ErrorDescription("name", "Peter does not start with J")))
    }
  }

  "contains validator" should {
    "validate a list with at least one valid element" in {
      val list = List("John", "Peter")
      startsWithJ.contains[List].validate(list) must beSuccessful(list)
    }

    "fail for a list with no valid elements" in {
      val list = List("Peter", "Paul")
      startsWithJ.contains[List].validate(list) must beFailing(NonEmptyList.of(ErrorDescription("list", "Peter does not start with J"), ErrorDescription("list", "Paul does not start with J")))
    }

    "fail for an empty list" in {
      val list = Nil
      startsWithJ.contains[List].validate(list) must beFailing(NonEmptyList.of(ErrorDescription("list", "Must be non empty")))
    }

    "validate a present optional" in {
      val name = Some("John")
      startsWithJ.contains[Option].validate(name) must beSuccessful(name)
    }

    "fail for an invalid optional" in {
      val name = Some("Peter")
      startsWithJ.contains[Option].validate(name) must beFailing(NonEmptyList.of(ErrorDescription("name", "Peter does not start with J")))
    }

    "fail for a missing optional" in {
      val name = None
      startsWithJ.contains[Option].validate(name) must beFailing(NonEmptyList.of(ErrorDescription("name", "Must be non empty")))
    }
  }

  "contains value validator" should {
    "validate a list containing the required element" in {
      val list = List("John", "Peter", "Paul")
      validator[List].contains("Paul").validate(list) must beSuccessful(list)
    }

    "fail for a list missing the required element" in {
      val list = List("John", "Simon", "James")
      validator[List].contains("Paul").validate(list) must beFailing(NonEmptyList.of(ErrorDescription("list", "Must contain Paul")))
    }

    "fail for an empty list" in {
      val list = Nil
      validator[List].contains("Paul").validate(list) must beFailing(NonEmptyList.of(ErrorDescription("list", "Must contain Paul")))
    }

    "validate a present optional" in {
      val name = Some("John")
      validator[Option].contains("John").validate(name) must beSuccessful(name)
    }

    "fail for an invalid optional" in {
      val name = Some("Peter")
      validator[Option].contains("John").validate(name) must beFailing(NonEmptyList.of(ErrorDescription("name", "Must contain John")))
    }

    "fail for a missing optional" in {
      val name = None
      validator[Option].contains("Paul").validate(name) must beFailing(NonEmptyList.of(ErrorDescription("name", "Must contain Paul")))
    }
  }

  "nonEmpty validator" should {
    "validate a list" in {
      val list = List(2)
      validator[List].nonEmpty.validate(list) must beSuccessful(list)
    }

    "fail for an empty list" in {
      val list = Nil
      validator[List].nonEmpty.validate(list) must beFailing(NonEmptyList.of(ErrorDescription("list", "Must be non empty")))
    }

    "validate a present optional" in {
      val name = Some("")
      validator[Option].nonEmpty.validate(name) must beSuccessful(name)
    }

    "fail for a missing optional" in {
      val name = None
      validator[Option].nonEmpty.validate(name) must beFailing(NonEmptyList.of(ErrorDescription("name", "Must be non empty")))
    }
  }
}
