package com.example.entities.authorization_code

import com.example.entities.authorization.AuthorizationId
import com.example.entities.client.ClientId
import com.example.entities.redirect_uri.RedirectURI
import com.example.entities.status.Status
import com.example.entities.{EntitiesError, EntitiesValidationResult}
import com.example.shared.ddd_base.Entity
import com.example.shared.util.EitherSyntax._

case class AuthorizationCode(
  id: AuthorizationCodeValue,
  status: Status,
  authorizationId: AuthorizationId,
  redirectURI: RedirectURI,
  clientId: ClientId
) extends Entity[AuthorizationCodeValue] {

  def authenticate(redirectURI: Option[String], clientId: ClientId): EntitiesValidationResult[AuthorizationCode] = {
    (assertRedirectURI(redirectURI), assertClientId(clientId)) match {
      case (Right(_), Right(_)) => this.asRight
      case (Right(_), Left(e))  => e.asLeft
      case (Left(e), _)         => e.asLeft
    }
  }

  private def assertRedirectURI(redirectURI: Option[String]): EntitiesValidationResult[RedirectURI] = {
    (redirectURI, this.redirectURI) match {
      case (Some(a), b) if a == b.value => this.redirectURI.asRight
      case (Some(a), b) if a != b.value => EntitiesError("un match redirect uri").asLeft
      case (None, _)                    => EntitiesError("required redirect uri").asLeft
    }
  }

  private def assertClientId(clientId: ClientId): EntitiesValidationResult[ClientId] = {
    if (clientId == this.clientId) clientId.asRight
    else EntitiesError("un match client id").asLeft
  }

}
