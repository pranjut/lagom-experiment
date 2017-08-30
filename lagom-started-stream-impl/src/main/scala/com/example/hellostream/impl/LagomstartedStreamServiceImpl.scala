package com.example.hellostream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.example.hellostream.api.LagomstartedStreamService
import com.example.hello.api.LagomstartedService

import scala.concurrent.Future

/**
  * Implementation of the LagomstartedStreamService.
  */
class LagomstartedStreamServiceImpl(lagomstartedService: LagomstartedService) extends LagomstartedStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagomstartedService.hello(_).invoke()))
  }
}
