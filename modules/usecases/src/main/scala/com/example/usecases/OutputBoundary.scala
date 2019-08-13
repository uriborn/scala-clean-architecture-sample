package com.example.usecases

import scala.concurrent.Future

trait OutputBoundary[OutputData] {

  def onComplete(result: Future[OutputData]): Unit

}
