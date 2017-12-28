package com.stuartizon.validator

import org.joda.time.{LocalDate, Period, PeriodType}
import cats.data.Validated._

trait DateValidators {
  val atLeast18: Validator[LocalDate] = new Validator[LocalDate] {
    override def validate(id: String, date: LocalDate): ValidationResult[LocalDate] = {
      val period = new Period(date, LocalDate.now, PeriodType.yearMonthDay)
      if (period.getYears >= 18) valid(date)
      else invalidNel(ErrorDescription(id, "Must be at least 18"))
    }
  }
}

object DateValidators extends DateValidators