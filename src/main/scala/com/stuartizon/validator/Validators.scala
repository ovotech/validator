package com.stuartizon.validator

trait Validators extends
  BasicValidators with
  MonadValidators with
  StringValidators

object Validators extends Validators