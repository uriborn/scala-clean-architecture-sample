package com.example.usecases.authorization

import com.example.entities.client.ClientId
import com.example.gateway.repositories.{ClientRepository, ReservedAuthorizationRepository}
import com.example.usecases.{OutputBoundary, UseCaseInteractor}
import com.example.usecases._
import com.google.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

case class AuthorizationAuthorizeInput(
  responseType: String,
  clientId: Long,
  redirectURI: Option[String],
  scope: Option[Seq[String]],
  state: Option[String]
)

case class AuthorizationAuthorizeOutput(
  redirectURI: Option[String],
  scope: Option[Seq[String]],
  state: Option[String],
  refKey: Option[String],
  clientId: Long,
  clientName: Option[String]
)

class AuthorizationAuthorizeUseCase @Inject()(
  outputBoundary: OutputBoundary[AuthorizationAuthorizeOutput],
  clientRepository: ClientRepository,
  reservedAuthorizationRepository: ReservedAuthorizationRepository
)(implicit ec: ExecutionContext) extends UseCaseInteractor[AuthorizationAuthorizeInput, AuthorizationAuthorizeOutput](outputBoundary) {

  override def dance(inputData: AuthorizationAuthorizeInput): Future[AuthorizationAuthorizeOutput] = {
    for {
      clientId     <- Future.successful(ClientId(inputData.clientId))
      client       <- clientRepository.findById(clientId)
      reservedAuth <- client.reservedAuthorization(inputData.responseType, inputData.redirectURI, inputData.scope, inputData.state).toF
      _            <- reservedAuthorizationRepository.store(reservedAuth)
    } yield AuthorizationAuthorizeOutput(
      redirectURI = Some(reservedAuth.redirectURI.value),
      scope = Some(reservedAuth.scopes.toStringList),
      state = reservedAuth.state.map(_.value),
      refKey = Some(reservedAuth.id.value),
      clientId = client.id.value,
      clientName = client.name.map(_.value)
    )
  }

}
