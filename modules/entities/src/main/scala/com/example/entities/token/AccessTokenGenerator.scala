package com.example.entities.token

import com.example.entities.client.ClientId
import com.example.entities.scope.Scopes

import scala.concurrent.Future

trait AccessTokenGenerator {

  def generate(scopes: Scopes, clientId: ClientId, accountID: String): Future[AccessToken]

}
