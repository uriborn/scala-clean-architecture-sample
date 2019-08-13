package com.example.shared.util

import scala.language.implicitConversions

object OptionSyntax extends OptionSyntax

trait OptionSyntax {
  implicit final def catsSyntaxOptionId[A](a: A): OptionIdOps[A] = new OptionIdOps(a)
}

final class OptionIdOps[A](private val a: A) extends AnyVal {
  def some: Option[A] = Some(a)
}
