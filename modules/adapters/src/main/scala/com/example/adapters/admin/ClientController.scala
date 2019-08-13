package com.example.adapters.admin

import com.example.usecases.admin.{ClientCreateInput, ClientCreateUseCase}
import com.google.inject.Inject

import scala.concurrent.Future

class ClientController @Inject()(
  clientCreateUseCase: ClientCreateUseCase,
  clientCreatePresenter: ClientCreatePresenter
) {

  def a(): Future[Any] = {
    val input = ClientCreateInput(name = None, redirectURIs = Nil, scopes = Nil)
    clientCreateUseCase.execute(input)
    clientCreatePresenter.response()
  }

}
