package com.example.infrastructure

import java.security.SecureRandom

import org.apache.commons.codec.binary.Hex

import scala.util.Random

object TokenGenerator {

  private val random = new Random(new SecureRandom())

  def generateTokenWithSizeOf(sizeInByte: Int): String = {
    val bytes = Array.ofDim[Byte](sizeInByte)
    random.nextBytes(bytes)
    new String(Hex.encodeHex(bytes))
  }

}
