package com.example.usecases.admin

import com.example.entities.EntitiesValidationResult
import com.example.entities.client.secret.SecretGenerator
import com.example.entities.client.{Client, ClientId}
import com.example.gateway.generators.IdGenerator
import com.example.gateway.repositories.ClientRepository
import com.example.usecases.{OutputBoundary, UseCaseInteractor, _}
import com.google.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

case class ClientCreateInput(name: Option[String], redirectURIs: Seq[String], scopes: Seq[String])

case class ClientCreateOutput(id: Long, secret: String)

class ClientCreateUseCase @Inject()(
   outputBoundary: OutputBoundary[ClientCreateOutput],
   clientIdGenerator: IdGenerator[ClientId],
   clientRepository: ClientRepository
)(implicit ec: ExecutionContext) extends UseCaseInteractor[ClientCreateInput, ClientCreateOutput](outputBoundary) {

  override def dance(inputData: ClientCreateInput): Future[ClientCreateOutput] = {
    for {
      id     <- clientIdGenerator.generateId
      client <- createClient(inputData, id).toF
      _      <- clientRepository.store(client)
    } yield ClientCreateOutput(client.id.value, client.secret.value)
  }

  private def createClient(inputData: ClientCreateInput, id: ClientId): EntitiesValidationResult[Client] = {
    val secret = SecretGenerator.generate
    Client.create(id, inputData.name, secret, inputData.redirectURIs, inputData.scopes)
  }

}
