package com.example.gateway.repositories

import com.example.entities.client.{Client, ClientId}
import com.example.shared.ddd_base.Repository

import scala.concurrent.Future

trait ClientRepository extends Repository[ClientId, Client] {
  def findAll: Future[Seq[Client]]
  def findById(clientId: ClientId): Future[Client]
  def store(entity: Client): Future[Long]
}
