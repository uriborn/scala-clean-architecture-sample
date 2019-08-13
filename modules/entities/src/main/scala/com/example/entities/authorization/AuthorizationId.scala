package com.example.entities.authorization

import com.example.shared.ddd_base.Identifier

case class AuthorizationId(value: Long) extends Identifier[Long]
