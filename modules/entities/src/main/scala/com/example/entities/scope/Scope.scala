package com.example.entities.scope

import com.example.entities.EnumWithValidation
import enumeratum.EnumEntry

import scala.collection.immutable

abstract sealed class Scope(override val entryName: String) extends EnumEntry

object Scope extends enumeratum.Enum[Scope] with EnumWithValidation[Scope] {
  def values: immutable.IndexedSeq[Scope] = findValues

  case object ReadOnly  extends Scope("read-only")
  case object ReadWrite extends Scope("read-write")
}
