package com.example.entities.client.ref_key

import com.example.infrastructure.TokenGenerator

object RefKeyGenerator {

  def generate: RefKey = {
    RefKey(TokenGenerator.generateTokenWithSizeOf(32))
  }

}
