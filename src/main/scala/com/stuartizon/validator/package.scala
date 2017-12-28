package com.stuartizon

import cats.data.ValidatedNel

package object validator {
  type ValidationResult[X] = ValidatedNel[ErrorDescription, X]
}