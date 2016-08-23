package com.ovoenergy.validator

import org.joda.time.{LocalDate, Period, PeriodType}

import scalaz.Scalaz._

trait DateValidators {
  val atLeast18 = new Validator[LocalDate] {
    override def validate(id: String, date: LocalDate): ValidationResult[LocalDate] = {
      val period = new Period(date, LocalDate.now, PeriodType.yearMonthDay)
      if (period.getYears >= 18) date.successNel
      else ErrorDescription(id, "Must be at least 18").failureNel
    }
  }
}

object DateValidators extends DateValidators