import akka.actor.{ActorRef, ActorSystem}
import akka.actor.actorRef2Scala
import com.typesafe.config.ConfigFactory
import com.walcron.twice.QueueReader
import com.walcron.twice.model.ApplicationSetting

import scala.language.implicitConversions

object Main {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load("application.conf").getConfig("com.walcron.twice")
    val kafkaConfig = config.getConfig("kafka")
    val applicationConfig = config.getConfig("application")

    val brokers = kafkaConfig.getString("brokers")
    val username = kafkaConfig.getString("username")
    val password = kafkaConfig.getString("password")
    val topic = kafkaConfig.getString("topic")
    val frequencyInMilliseconds = kafkaConfig.getLong("frequency-in-miliseconds")

    val mode = applicationConfig.getInt("mode")
    val restUrl = applicationConfig.getString("rest-url")
    val lejosUrl = applicationConfig.getString("lejos-pan-ip")
    val lejosPort = applicationConfig.getInt("lejos-pan-port")
    val applicationSetting = ApplicationSetting(mode, restUrl, lejosUrl, lejosPort)

    val system: ActorSystem = ActorSystem("twice")
    val props = QueueReader.props(applicationSetting, brokers, username, password, Array(topic), frequencyInMilliseconds)
    val controller: ActorRef = system.actorOf(props, "twiceDotty")
    controller ! QueueReader.Go
  }
}
