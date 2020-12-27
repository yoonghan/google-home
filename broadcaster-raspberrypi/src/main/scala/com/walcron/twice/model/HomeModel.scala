package com.walcron.twice.model

case class KafkaHome(
                    action: String,
                    triggerTime: String
                    )

case class HomeActions(
                      isRunning: Option[Boolean],
                      on: Option[Boolean]
                      )

case class ApplicationSetting (
                              mode: Int,
                              restUrl: String,
                              lejosUrl: String,
                              lejosPort: Int
                              )