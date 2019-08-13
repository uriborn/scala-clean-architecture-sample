package com.example.usecases

trait InputBoundary[InputData] {

  def execute(inputData: InputData): Unit

}
