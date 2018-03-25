package com.stuartizon.validator

import cats.data.Validated._
import java.time.{LocalDate, Period}

trait DateValidators {
  val ageMessage = "Must be at least 18"

  val atLeast18 = new Validator[LocalDate] {
    def validate(id: String, date: LocalDate): ValidationResult[LocalDate] = {
      val period = Period.between(date, LocalDate.now)
      if (period.getYears >= 18) valid(date)
      else invalidNel(ErrorDescription(id, ageMessage))
    }
  }
}

object DateValidators extends DateValidators