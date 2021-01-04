package com.walcron.twice.api

import java.io._
import java.net._

class LejosCall(ipAddress: String, port: Int) {
  def triggerDevice(shouldRun: Boolean):Unit = {
    val inetConnection = new InetSocketAddress(ipAddress, port)
    val clientSocket = new Socket()
    clientSocket.connect(inetConnection, 5000)
    val out = new PrintWriter(clientSocket.getOutputStream, true)

    if(shouldRun) {
      out.write(createUnregulatedMovement("CX",-50,1))//open
      out.write(createRegulatedMovement("AB","L",360,360,0,0))
      out.write(createUnregulatedMovement("CX",50,1))//close
      out.write(createRegulatedMovement("AB","L",-420,420,0,0))//turn
      out.write(createRegulatedMovement("AB","L",2900,2900,0,900))
      out.write(createUnregulatedMovement("CX",-50,1))//close
      out.write(createRegulatedMovement("AB","L",-180,-180,0,0))
    }
    else {
      out.write(createRegulatedMovement("AB","L",180,180,0,0))
      out.write(createUnregulatedMovement("CX",50,1))//close
      out.write(createRegulatedMovement("AB","L",805,-805,0,0))
      out.write(createRegulatedMovement("AB","L",2900,2900,0,900)) 
      out.write(createRegulatedMovement("AB","L",440,-440,0,0))
      out.write(createRegulatedMovement("AB","L",-180,-180,0,0))
      out.write(createUnregulatedMovement("CX",-50,1))
      out.write(createRegulatedMovement("AB","L",360,360,0,0))
    }

    out.flush
    out.close
    clientSocket.close
  }

  def createRegulatedMovement(motors:String, motorType:String, rotation1:Int, rotation2:Int, acceleration:Int, speed:Int):String = {
    String.format("%s:%s:%04d%04d%04d%03d",motors,motorType,rotation1,rotation2,acceleration,speed)
  }

  def createUnregulatedMovement(motors:String, power:Int, delay:Int):String = {
    String.format("%s:U:%04d%03d00000000",motors,power,delay)
  }

  def stop(): Unit = {
  }
}
