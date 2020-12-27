package com.walcron.twice.api

import java.io._
import java.net._

class LejosCall(ipAddress: String, port: Int) {
  def triggerDevice(shouldRun: Boolean):Unit = {
    import java.io.PrintWriter
    import java.net.Socket

    if(shouldRun) {
      val inetConnection = new InetSocketAddress(ipAddress, port)
      val clientSocket = new Socket()
      clientSocket.connect(inetConnection, 5000)
      val out = new PrintWriter(clientSocket.getOutputStream, true)

      out.write("A:L:03601000100")
      out.close()
      clientSocket.close()
    }
  }

  def stop(): Unit = {

  }
}
