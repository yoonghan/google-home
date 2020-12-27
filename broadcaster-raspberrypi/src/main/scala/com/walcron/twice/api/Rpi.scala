package com.walcron.twice.api

import com.pi4j.io.gpio._

object Rpi {

  val gpio = GpioFactory.getInstance

  val relay1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "Locker 1", PinState.HIGH)
  val relay2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "Locker 2", PinState.HIGH)
  val relay3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "Locker 3", PinState.HIGH)
  println(s"""Initiate locker: 1=25, 2=28, 3=29""")

  relay1.setShutdownOptions(true, PinState.LOW)
  relay2.setShutdownOptions(true, PinState.LOW)

  private def sendSignal(locker:GpioPinDigitalOutput, isSwitchOn:Boolean):Unit = {
    if(isSwitchOn) {
      locker.low
    }
    else {
      locker.high
    }
  }

  def isToLock(lockerState:String):Boolean = (lockerState == "lock")

  def triggerActionRequest(isSwitchOn:Option[Boolean], isRunning: Option[Boolean]):Boolean = {
     try {
       if(isSwitchOn.isDefined) {
         sendSignal(relay1, isSwitchOn.get)
       }
       if(isRunning.isDefined) {
         sendSignal(relay2, isRunning.get)
       }
       return true
     }
     catch {
        case ex:Exception => {
          println("Encountered exception")
        }
     }
     false
  }

  def shutdown():Unit = {
    gpio.shutdown
  }

}
