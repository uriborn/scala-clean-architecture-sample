package com.example.usecases.authorization

import com.example.entities.authorization.AuthorizationId
import com.example.entities.reserved_authorization.ReservedAuthorizationId
import com.example.gateway.generators.IdGenerator
import com.example.gateway.repositories.{AuthorizationCodeRepository, AuthorizationRepository, ReservedAuthorizationRepository}
import com.example.usecases.{OutputBoundary, UseCaseInteractor}
import com.example.usecases._
import com.google.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

case class AuthorizationApproveInput(
  refKey: String,
  clientId: Long,
  scopes: Option[Seq[String]],
  accountId: String
)

case class AuthorizationApproveOutput(
  redirectURI: Option[String],
  state: Option[String],
  code: Option[String],
  accountId: Option[String]
)

class AuthorizationApproveUseCase @Inject()(
  outputBoundary: OutputBoundary[AuthorizationApproveOutput],
  authorizationIdGenerator: IdGenerator[AuthorizationId],
  reservedAuthorizationRepository: ReservedAuthorizationRepository,
  authorizationCodeRepository: AuthorizationCodeRepository,
  authorizationRepository: AuthorizationRepository
)(implicit ec: ExecutionContext) extends UseCaseInteractor[AuthorizationApproveInput, AuthorizationApproveOutput](outputBoundary) {

  override def dance(inputData: AuthorizationApproveInput): Future[AuthorizationApproveOutput] = {
    for {
      reservedAuthId  <- Future.successful(ReservedAuthorizationId(inputData.refKey))
      reservedAuth    <- reservedAuthorizationRepository.findById(reservedAuthId)
      authorizationId <- authorizationIdGenerator.generateId
      authTuple       <- reservedAuth.approve(authorizationId, inputData.clientId, inputData.scopes, inputData.accountId).toF
      (authCode, auth) = authTuple
      _               <- authorizationCodeRepository.store(authCode)
      _               <- authorizationRepository.store(auth)
      _               <- reservedAuthorizationRepository.delete(reservedAuthId)
    } yield AuthorizationApproveOutput(
      redirectURI = Some(authCode.redirectURI.value),
      state = reservedAuth.state.map(_.value),
      code = Some(authCode.id.value),
      accountId = Some(auth.accountId)
    )
  }

}
