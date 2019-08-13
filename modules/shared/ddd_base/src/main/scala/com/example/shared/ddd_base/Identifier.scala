package com.example.shared.ddd_base

trait Identifier[+A] {
  def value: A
  def hashCode: Int
  def equals(that: Any): Boolean
}
