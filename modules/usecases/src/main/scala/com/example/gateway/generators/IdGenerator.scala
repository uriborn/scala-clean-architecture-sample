package com.example.gateway.generators

import scala.concurrent.Future

trait IdGenerator[ID] {

  def generateId: Future[ID]

}
