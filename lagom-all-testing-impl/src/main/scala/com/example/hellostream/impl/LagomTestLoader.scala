package com.example.hellostream.impl

import com.example.hello.api.LagomstartedService
import com.example.hellostream.api.{LagomstartedStreamService, LagomTestService}
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class LagomTestLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomTestApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomTestApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomTestService])
}

abstract class LagomTestApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[LagomTestService](wire[LagomTestServiceImpl])

  // Bind the LagomstartedService client
  lazy val lagomstartedService = serviceClient.implement[LagomstartedService]

  lazy val lagomStreamService = serviceClient.implement[LagomstartedStreamService]
}
