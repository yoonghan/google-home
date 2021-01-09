package com.walcron.twice.api

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class RestCall(url:String) {

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  def triggerRest(action: String):Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"""${url}/api/trigger""",
      entity = HttpEntity(ContentTypes.`application/json`, action),
      headers = Seq()
    )

    Http().singleRequest(request)
    printf("Request sent to %s", url)
  }

}
