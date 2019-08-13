package com.example.entities.token

import com.example.entities.scope.Scopes

case class Token(
  accessToken: AccessToken,
  tokenType: TokenType,
  expiresIn: ExpiresIn,
  refreshToken: RefreshToken,
  scopes: Scopes
)
