package com.example.entities.authorization_code

import com.example.shared.ddd_base.Identifier

case class AuthorizationCodeValue(value: String) extends Identifier[String]
