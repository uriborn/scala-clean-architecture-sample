package com.example.entities.client

import java.time.ZonedDateTime

//import cats.data.NonEmptyList
//import cats.implicits._
import com.example.entities.{EntitiesError, EntitiesValidationResult}
import com.example.entities.client.ref_key.RefKeyGenerator
import com.example.entities.client.secret.Secret
import com.example.entities.redirect_uri.RedirectURI
import com.example.entities.reserved_authorization.response_type.ResponseType
import com.example.entities.reserved_authorization.{ReservedAuthorization, ReservedAuthorizationId}
import com.example.entities.scope.Scopes
import com.example.entities.state.State
import com.example.entities.status.Status
import com.example.shared.ddd_base.Entity
import com.example.shared.util.EitherSyntax._

case class Client(
  id: ClientId,
  name: Option[ClientName],
  secret: Secret,
  redirectURIs: List[String],
  scopes: Scopes,
  status: Status,
  createdAt: ZonedDateTime,
  updatedAt: Option[ZonedDateTime]
) extends Entity[ClientId] {

  def reservedAuthorization(responseType: String,
                            redirectURI: Option[String],
                            scope: Option[Seq[String]],
                            state: Option[String]): EntitiesValidationResult[ReservedAuthorization] = {
    (assertResponseType(responseType), assertRedirectURI(redirectURI), assertScope(scope), assertState(state)) match {
      case (Right(_responseType), Right(_redirectURI), Right(_scopes), Right(_state)) =>
        ReservedAuthorization(
          id = ReservedAuthorizationId(RefKeyGenerator.generate.value),
          status = Status.Active,
          responseType = _responseType,
          clientId = this.id,
          redirectURI = _redirectURI,
          scopes = _scopes,
          state = _state,
          createdAt = ZonedDateTime.now(),
          updatedAt = None
        ).asRight
      case (Left(e), _, _, _) => e.asLeft
      case (_, Left(e), _, _) => e.asLeft
      case (_, _, Left(e), _) => e.asLeft
      case (_, _, _, Left(e)) => e.asLeft
    }
  }

  def authenticate(password: String): EntitiesValidationResult[Client] = {
    assertSecret(password).map(_ => this)
  }

  private def assertResponseType(responseType: String): EntitiesValidationResult[ResponseType] = {
    if (responseType == ResponseType.Code.entryName) ResponseType.Code.asRight
    else EntitiesError("response type is only 'code'").asLeft
  }

  private def assertRedirectURI(redirectURI: Option[String]): EntitiesValidationResult[RedirectURI] = {
    redirectURI.map(RedirectURI).getOrElse(RedirectURI(this.redirectURIs.head)).asRight
  }

  private def assertScope(scope: Option[Seq[String]]): EntitiesValidationResult[Scopes] = {
    Scopes.fromOptSeqString(scope) map {
      case Some(s) => s
      case None => this.scopes
    }
  }

  private def assertState(state: Option[String]): EntitiesValidationResult[Option[State]] = {
    state.map(State).asRight
  }

  private def assertSecret(secret: String): EntitiesValidationResult[Secret] = {
    if (secret == this.secret.value) this.secret.asRight
    else EntitiesError("invalid secret").asLeft
  }

}

object Client {

  def create(id: ClientId,
             name: Option[String],
             secret: Secret,
             redirectURIs: Seq[String],
             scopes: Seq[String]): EntitiesValidationResult[Client] = {
    (assertName(name), assertRedirectURIs(redirectURIs), assertScopes(scopes)) match {
      case (Right(_clientName), Right(_redirectURIs), Right(_scopes)) =>
        Client(
          id = id,
          name = _clientName,
          secret = secret,
          redirectURIs = _redirectURIs,
          scopes = _scopes,
          status = Status.Active,
          createdAt = ZonedDateTime.now(),
          updatedAt = None
        ).asRight
      case (Left(e), _, _) => e.asLeft
      case (_, Left(e), _) => e.asLeft
      case (_, _, Left(e)) => e.asLeft
    }
  }

  private def assertName(name: Option[String]): EntitiesValidationResult[Option[ClientName]] = {
    if (name.exists(_.length <= 50)) name.map(ClientName).asRight
    else EntitiesError("name fields maximum length from 50 characters").asLeft
  }

  private def assertRedirectURIs(redirectURIs: Seq[String]): EntitiesValidationResult[List[String]] = {
    if (redirectURIs.nonEmpty) redirectURIs.toList.asRight
    else EntitiesError("redirect uris fields is empty").asLeft
  }

  private def assertScopes(scopes: Seq[String]): EntitiesValidationResult[Scopes] = {
    Scopes.fromSeqString(scopes)
  }

}
