package com.example.shared.ddd_base

trait Entity[ID <: Identifier[_]] {
  type ID

  def hashCode: Int
  def equals(that: Any): Boolean
}
