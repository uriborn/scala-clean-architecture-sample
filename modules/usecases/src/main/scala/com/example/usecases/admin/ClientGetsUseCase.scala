package com.example.usecases.admin

import com.example.gateway.repositories.ClientRepository
import com.example.usecases.{OutputBoundary, UseCaseInteractor}
import com.google.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

case class ClientGetsInput()

case class ClientOutput(
  id: Long,
  name: Option[String],
  redirectURIs: Seq[String],
  scopes: Seq[String],
  createdAt: Long,
  updatedAt: Option[Long]
)

case class ClientGetsOutput(clients: Seq[ClientOutput])

class ClientGetsUseCase @Inject()(
  outputBoundary: OutputBoundary[ClientGetsOutput],
  clientRepository: ClientRepository
)(implicit ec: ExecutionContext) extends UseCaseInteractor[ClientGetsInput, ClientGetsOutput](outputBoundary) {

  override def dance(inputData: ClientGetsInput): Future[ClientGetsOutput] = {
    clientRepository.findAll.map { clients =>
      ClientGetsOutput(clients.map { client =>
       ClientOutput(
         id = client.id.value,
         name = client.name.map(_.value),
         redirectURIs = client.redirectURIs,
         scopes = client.scopes.toStringList,
         createdAt = client.createdAt.toInstant.toEpochMilli,
         updatedAt = client.updatedAt.map(_.toInstant.toEpochMilli)
       )
      })
    }
  }

}
