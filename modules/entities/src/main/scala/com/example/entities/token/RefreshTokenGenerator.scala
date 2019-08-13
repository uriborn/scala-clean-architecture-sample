package com.example.entities.token

import scala.concurrent.Future

trait RefreshTokenGenerator {

  def generate: Future[RefreshToken]

}
