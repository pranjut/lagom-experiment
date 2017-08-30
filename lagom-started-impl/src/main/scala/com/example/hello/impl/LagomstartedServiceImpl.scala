package com.example.hello.impl

import com.example.hello.api
import com.example.hello.api.{LagomstartedService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the LagomstartedService.
  */
class LagomstartedServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends LagomstartedService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the lagom-started entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagomstartedEntity](id)

    println(s"......................$id")
    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the lagom-started entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagomstartedEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(LagomstartedEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[LagomstartedEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
