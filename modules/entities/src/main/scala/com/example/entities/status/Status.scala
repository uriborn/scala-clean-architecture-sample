package com.example.entities.status

import com.example.entities.EnumWithValidation
import enumeratum.EnumEntry

import scala.collection.immutable

sealed abstract class Status(override val entryName: String) extends EnumEntry

object Status extends enumeratum.Enum[Status] with EnumWithValidation[Status] {
  def values: immutable.IndexedSeq[Status] = findValues

  case object Active  extends Status("active")
  case object Suspend extends Status("suspend")
  case object Deleted extends Status("deleted")
}
