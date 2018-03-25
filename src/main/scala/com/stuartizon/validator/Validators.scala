package com.stuartizon.validator

trait Validators extends
  BasicValidators with
  MonadValidators with
  StringValidators with
  NumberValidators

object Validators extends Validators