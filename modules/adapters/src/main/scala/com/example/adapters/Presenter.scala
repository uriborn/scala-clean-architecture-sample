package com.example.adapters

import com.example.shared.util.EitherSyntax._
import com.example.usecases.OutputBoundary

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

trait Presenter[OutputData] extends OutputBoundary[OutputData] {

  implicit val ec: ExecutionContext

  private val promise: Promise[OutputData] = Promise[OutputData]

  override def onComplete(result: Future[OutputData]): Unit = {
    result.onComplete {
      case Success(value) => promise.success(value)
      case Failure(cause) => promise.failure(cause)
    }
  }

  type ResponseQuery = String
  type ErrorResponse = String
  type Response = Either[ErrorResponse, ResponseQuery]

  protected def convert(result: OutputData): String
  protected def convertError(): ErrorResponse

  def response(): Future[Response] = {
    promise.future
      .map(value => convert(value).asRight)
      .recover { case _ => convertError().asLeft }
  }

}
