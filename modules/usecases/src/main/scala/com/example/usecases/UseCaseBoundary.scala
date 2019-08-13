package com.example.usecases

import scala.concurrent.Future

trait UseCaseBoundary[InputData, OutputData] {

  def execute(inputData: InputData): Future[OutputData]

}
