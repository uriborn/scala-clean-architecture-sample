package com.example

import com.example.entities.EntitiesValidationResult

import scala.concurrent.Future

package object usecases {

  implicit class EntitiesError2Future[A](val result: EntitiesValidationResult[A]) extends AnyVal {
    def toF: Future[A] = {
      result.fold(
        error => Future.failed(new Exception(error.message)),
        s     => Future.successful(s)
      )
    }
  }

}
