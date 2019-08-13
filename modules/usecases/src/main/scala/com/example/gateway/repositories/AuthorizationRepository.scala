package com.example.gateway.repositories

import com.example.entities.authorization.{Authorization, AuthorizationId}
import com.example.shared.ddd_base.Repository

import scala.concurrent.Future

trait AuthorizationRepository extends Repository[AuthorizationId, Authorization] {
  def store(entity: Authorization): Future[Long]
}
