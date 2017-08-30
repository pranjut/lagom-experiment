package com.example.hellostream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.hellostream.api.LagomstartedStreamService
import com.example.hello.api.LagomstartedService
import com.softwaremill.macwire._

class LagomstartedStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomstartedStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomstartedStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomstartedStreamService])
}

abstract class LagomstartedStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[LagomstartedStreamService](wire[LagomstartedStreamServiceImpl])

  // Bind the LagomstartedService client
  lazy val lagomstartedService = serviceClient.implement[LagomstartedService]
}
