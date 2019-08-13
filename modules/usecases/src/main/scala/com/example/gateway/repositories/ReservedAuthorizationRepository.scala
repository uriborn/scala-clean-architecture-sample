package com.example.gateway.repositories

import com.example.entities.reserved_authorization.{ReservedAuthorization, ReservedAuthorizationId}
import com.example.shared.ddd_base.Repository

import scala.concurrent.Future

trait ReservedAuthorizationRepository extends Repository[ReservedAuthorizationId, ReservedAuthorization] {
  def findById(id: ReservedAuthorizationId): Future[ReservedAuthorization]
  def store(entity: ReservedAuthorization): Future[Long]
  def delete(id: ReservedAuthorizationId): Future[Long]
}
