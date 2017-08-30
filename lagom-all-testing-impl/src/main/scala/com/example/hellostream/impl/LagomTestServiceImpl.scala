package com.example.hellostream.impl

import com.example.hello.api.LagomstartedService
import com.example.hellostream.api.{LagomstartedStreamService, LagomTestService}
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

/**
  * Implementation of the LagomstartedStreamService.
  */
class LagomTestServiceImpl(lagomstartedService: LagomstartedService, lagomStreamService: LagomstartedStreamService) extends LagomTestService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagomstartedService.hello(_).invoke()))
  }

  def test(id: String) = {

    for(i <- 1 to 10) {
      println(s"Hello $id")
      Thread.sleep(1000 * 2)
    }

    Future.successful(s"Hello $id")
  }

  test("Pranjut Protim Gogoi")
}
