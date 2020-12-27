package com.walcron.twice.api

import java.io.PrintWriter
import java.net.Socket

class LejosCall(ipAddress: String, port: Int) {
  val clientSocket = new Socket(ipAddress, port)
  val out = new PrintWriter(clientSocket.getOutputStream, true)

  def triggerDevice(shouldRun: Boolean):Unit = {
    import java.io.PrintWriter
    import java.net.Socket

    if(shouldRun) {
      out.write("A:L:03601000100")
    }
  }

  def stop(): Unit = {

  }
}
