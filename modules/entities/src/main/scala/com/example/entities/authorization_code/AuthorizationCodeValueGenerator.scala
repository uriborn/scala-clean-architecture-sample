package com.example.entities.authorization_code

import com.example.infrastructure.TokenGenerator

object AuthorizationCodeValueGenerator {

  def generate: AuthorizationCodeValue = {
    AuthorizationCodeValue(TokenGenerator.generateTokenWithSizeOf(64))
  }

}
