package com.example.adapters.admin

import com.example.adapters.Presenter
import com.example.usecases.admin.ClientCreateOutput
import com.google.inject.Inject

import scala.concurrent.ExecutionContext

class ClientCreatePresenter @Inject()(implicit val ec: ExecutionContext) extends Presenter[ClientCreateOutput] {

  override protected def convert(result: ClientCreateOutput): String = ""
  override protected def convertError(): String = ""

}
