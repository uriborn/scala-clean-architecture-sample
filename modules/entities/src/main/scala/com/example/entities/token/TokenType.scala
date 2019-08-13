package com.example.entities.token

import com.example.entities.EnumWithValidation
import enumeratum.EnumEntry

import scala.collection.immutable

sealed abstract class TokenType(override val entryName: String) extends EnumEntry

object TokenType extends enumeratum.Enum[TokenType] with EnumWithValidation[TokenType] {
  def values: immutable.IndexedSeq[TokenType] = findValues

  case object Bearer extends TokenType("Bearer")
}
