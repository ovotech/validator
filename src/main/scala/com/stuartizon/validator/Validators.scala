package com.stuartizon.validator

trait Validators extends
  BasicValidators with
  DateValidators with
  MonadValidators with
  StringValidators

object Validators extends Validators