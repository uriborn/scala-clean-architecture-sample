package com.example.entities.reserved_authorization

import java.time.ZonedDateTime

import com.example.entities.authorization.{Authorization, AuthorizationId}
import com.example.entities.authorization_code.{AuthorizationCode, AuthorizationCodeValueGenerator}
import com.example.entities.client.ClientId
import com.example.entities.redirect_uri.RedirectURI
import com.example.entities.reserved_authorization.response_type.ResponseType
import com.example.entities.scope.Scopes
import com.example.entities.state.State
import com.example.entities.status.Status
import com.example.entities.{EntitiesError, EntitiesValidationResult}
import com.example.shared.ddd_base.Entity
import com.example.shared.util.EitherSyntax._

case class ReservedAuthorization(
  id: ReservedAuthorizationId,
  responseType: ResponseType,
  clientId: ClientId,
  redirectURI: RedirectURI,
  scopes: Scopes,
  state: Option[State],
  status: Status,
  createdAt: ZonedDateTime,
  updatedAt: Option[ZonedDateTime]
) extends Entity[ReservedAuthorizationId] {

  def approve(authorizationId: AuthorizationId,
              clientId: Long,
              scope: Option[Seq[String]],
              accountId: String): EntitiesValidationResult[(AuthorizationCode, Authorization)] = {
    (assertClientId(clientId), assertScope(scope)) match {
      case (Right(_clientId), Right(_scopes)) =>
        (
          AuthorizationCode(
            id = AuthorizationCodeValueGenerator.generate,
            status = Status.Active,
            authorizationId = authorizationId,
            redirectURI = this.redirectURI,
            clientId = _clientId
          ),
          Authorization(
            id = authorizationId,
            status = Status.Active,
            clientId = _clientId,
            scopes = _scopes,
            accountId = accountId,
            refreshToken = None,
            createdAt = ZonedDateTime.now(),
            updatedAt = None
          )
        ).asRight
      case (Left(e), _) => e.asLeft
    }
  }

  def deny(clientId: Long): EntitiesValidationResult[ReservedAuthorization] = {
    assertClientId(clientId).map(_ => this)
  }

  private def assertClientId(clientId: Long): EntitiesValidationResult[ClientId] = {
    if (clientId == this.clientId.value) this.clientId.asRight
    else EntitiesError("client id is wrong").asLeft
  }

  private def assertScope(scope: Option[Seq[String]]): EntitiesValidationResult[Scopes] = {
    Scopes.fromOptSeqString(scope).map {
      case Some(s) => s
      case None    => this.scopes
    }
  }


}
