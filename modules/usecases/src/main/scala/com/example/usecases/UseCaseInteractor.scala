package com.example.usecases

import scala.concurrent.{ExecutionContext, Future}

abstract class UseCaseInteractor[InputData, OutputData](
  protected val outputBoundary: OutputBoundary[OutputData]
)(implicit ec: ExecutionContext) extends InputBoundary[InputData] {

    protected def dance(inputData: InputData): Future[OutputData]

    override def execute(inputData: InputData): Unit = outputBoundary.onComplete(dance(inputData))

}
