package com.walcron.twice

import java.time.Duration
import java.util.Properties

import scala.language.implicitConversions
import akka.actor.{Actor, Props, Terminated, actorRef2Scala}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.walcron.twice.QueueReader.Go
import com.walcron.twice.api._
import com.walcron.twice.model.{ApplicationSetting, HomeActions, KafkaHome}
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

class QueueReader(applicationSetting: ApplicationSetting, brokers: String, username: String, password: String, topics: Array[String], frequencyInMilliseconds: Long) extends Actor {
  println("Started Queue Reader")

  private val serializer: String = classOf[StringSerializer].getName
  private val deserializer: String = classOf[StringDeserializer].getName
  private val jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";"
  private val jaasCfg: String = String.format(jaasTemplate, username, password)

  private val props = new Properties()
  props.put("bootstrap.servers", brokers)
  props.put("group.id", username + "-consumer")
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "1000")
  props.put("auto.offset.reset", "earliest")
  props.put("session.timeout.ms", "30000")
  props.put("key.deserializer", deserializer)
  props.put("value.deserializer", deserializer)
  props.put("key.serializer", serializer)
  props.put("value.serializer", serializer)
  props.put("security.protocol", "SASL_SSL")
  props.put("sasl.mechanism", "SCRAM-SHA-256")
  props.put("sasl.jaas.config", jaasCfg)

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  def startConsume(): Unit = {
    println("Started To Consume")
    import scala.jdk.CollectionConverters._
    import scala.jdk.javaapi.CollectionConverters._

    val consumer = new KafkaConsumer(props)
    consumer.subscribe(asJavaCollection(topics))
    while (true) {
      val records = consumer.poll(Duration.ofMillis(frequencyInMilliseconds)).asScala.toVector
      for (record <- records) {
        printf("### %s [%d] offset=%d, key=%s, value=\"%s\"\n", record.topic, record.partition, record.offset, record.key, record.value)
        val value: String = record.value
        try {
          val result = mapper.readValue(value, classOf[KafkaHome])
          val actionValue = result.action
          val homeAction = mapper.readValue(actionValue, classOf[HomeActions])

          applicationSetting.mode match {
            case 2 => {
              lazy val proxyAndCallUrl = new RestCall(applicationSetting.restUrl)
              proxyAndCallUrl.triggerRest( actionValue )
            }
            case 3 => {
              Rpi.triggerActionRequest(homeAction.on, homeAction.isRunning)
            }
            case 4 => {
              Rpi.triggerActionRequest(homeAction.on, homeAction.isRunning)
              lazy val proxyAndCallUrl = new RestCall(applicationSetting.restUrl)
              proxyAndCallUrl.triggerRest( actionValue )
            }
            case 5 => {
              lazy val lejosCall = new LejosCall(applicationSetting.lejosUrl, applicationSetting.lejosPort)

              if(homeAction.on.isDefined) {
                lejosCall.triggerDevice(homeAction.on.get)
              }
            }
            case _ => {}
          }
        }
        catch {
          case e:Exception => e.printStackTrace
        }
      }
    }
  }

  override def receive = {
    case Go => {
     startConsume()
    }
    case Terminated(who) => {
      self ! Go
    }
  }
}

object QueueReader {
  def props(applicationSetting: ApplicationSetting, brokers: String, username: String, password: String, topics: Array[String], frequencyInMilliseconds: Long):Props =
    Props(new QueueReader(applicationSetting, brokers, username, password, topics, frequencyInMilliseconds))
  case object Go
}
