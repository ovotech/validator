package com.ovoenergy.validator

import scala.reflect.macros.blackbox

private object ValidationMacros {
  def validate[X: c.WeakTypeTag](c: blackbox.Context)(value: c.Expr[X]) = {
    import c.universe._
    c.Expr[ValidationResult[X]] {
      q"""${c.prefix}.validate(${id(show(value.tree))}, $value)"""
    }
  }

  private val complexFieldRegex = "^(\\w+\\.)+(\\w+)$".r

  private def id: String => String = {
    // e.g. take 'value' if the tree resolves to 'parent.object.value'
    case complexFieldRegex(_, suffix) => suffix
    case string => string
  }
}