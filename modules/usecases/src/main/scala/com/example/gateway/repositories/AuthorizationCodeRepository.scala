package com.example.gateway.repositories

import com.example.entities.authorization_code.{AuthorizationCode, AuthorizationCodeValue}
import com.example.shared.ddd_base.Repository

import scala.concurrent.Future

trait AuthorizationCodeRepository extends Repository[AuthorizationCodeValue, AuthorizationCode] {
  def store(entity: AuthorizationCode): Future[Long]
}
