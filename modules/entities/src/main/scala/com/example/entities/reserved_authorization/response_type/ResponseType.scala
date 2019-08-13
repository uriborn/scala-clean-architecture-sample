package com.example.entities.reserved_authorization.response_type

import com.example.entities.EnumWithValidation
import enumeratum.EnumEntry

import scala.collection.immutable

sealed abstract class ResponseType(override val entryName: String) extends EnumEntry

object ResponseType extends enumeratum.Enum[ResponseType] with EnumWithValidation[ResponseType] {
  def values: immutable.IndexedSeq[ResponseType] = findValues

  case object Code extends ResponseType("code")
}
