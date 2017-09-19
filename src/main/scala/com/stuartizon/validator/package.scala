package com.stuartizon

import scalaz.ValidationNel

package object validator {
  type ValidationResult[X] = ValidationNel[ErrorDescription, X]
}