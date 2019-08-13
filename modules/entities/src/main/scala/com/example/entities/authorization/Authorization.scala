package com.example.entities.authorization

import java.time.ZonedDateTime

import scala.concurrent.{ExecutionContext, Future}

//import cats.Monad
//import cats.data.Validated.{Invalid, Valid}
//import cats.implicits._
import com.example.shared.util.EitherSyntax._
import com.example.entities.{EntitiesError, EntitiesValidationResult}
import com.example.entities.client.ClientId
import com.example.entities.scope.Scopes
import com.example.entities.status.Status
import com.example.entities.token._
import com.example.shared.ddd_base.Entity

import scala.language.higherKinds

case class Authorization(
  id: AuthorizationId,
  clientId: ClientId,
  scopes: Scopes,
  accountId: String,
  refreshToken: Option[RefreshToken],
  status: Status,
  createdAt: ZonedDateTime,
  updatedAt: Option[ZonedDateTime]
) extends Entity[AuthorizationId] {

  def generateToken(accessTokenGenerator: AccessTokenGenerator,
                    refreshTokenGenerator: RefreshTokenGenerator)
                   (implicit ec: ExecutionContext): Future[(Authorization, Token)] = {
    for {
      accessToken  <- accessTokenGenerator.generate(this.scopes, this.clientId, this.accountId)
      refreshToken <- refreshTokenGenerator.generate
    } yield {
      val token = Token(
        accessToken = accessToken,
        tokenType = TokenType.Bearer,
        expiresIn = ExpiresIn(3600),
        refreshToken = refreshToken,
        scopes = this.scopes
      )
      val auth = this.copy(refreshToken = Some(token.refreshToken))

      (auth, token)
    }
  }

  def authenticate(clientId: ClientId): EntitiesValidationResult[VerifiedAuthorization] = {
    assertClientId(clientId).map(_ => new VerifiedAuthorization(this))
  }

  private def assertClientId(clientId: ClientId): EntitiesValidationResult[ClientId] = {
    if (clientId == this.clientId) clientId.asRight
    else EntitiesError("un match clientId").asLeft
  }

  class VerifiedAuthorization private[Authorization](authorization: Authorization) {

    def generateTokenByRefreshToken(
      accessTokenGenerator: AccessTokenGenerator,
      refreshToken: RefreshToken,
      scopeOptSeqString: Option[Seq[String]]
    )(implicit ec: ExecutionContext): Future[EntitiesValidationResult[Token]] = {
      Scopes.fromOptSeqString(scopeOptSeqString) match {
        case Left(e) => Future.successful(e.asLeft)
        case Right(scopesOpt) =>
          val s = scopesOpt.getOrElse(authorization.scopes)

          for {
            accessToken <- accessTokenGenerator.generate(s, authorization.clientId, authorization.accountId)
          } yield Token(
            accessToken = accessToken,
            tokenType = TokenType.Bearer,
            expiresIn = ExpiresIn(3600),
            refreshToken = refreshToken,
            scopes = s
          ).asRight
      }
    }
  }

}
