package com.walcron.lejos.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TestTriggerClient {

	private final int PORT = 9000;
	private final String HOST_NAME = "10.0.1.1";
	
	public TestTriggerClient() {
		try (
		    Socket clientSocket = new Socket(HOST_NAME, PORT);
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		){
			clientSocket.setKeepAlive(false);
			clientSocket.setSoTimeout(30_000);
			
		    out.write("AX:L:036000002000900"); //forward with control
		    out.write("BX:L:-36000000000000"); //default backward
		    out.write("CX:M:036000000000200"); //speed change
		    out.write("AB:L:072007202000900"); //synch
		    out.write("AB:L:0720-7200000900"); //turn
		    
		    //Only allow motor 1, rotation is replaced as power, positive forward, negative backward.
			out.write("CX:U:-05000100000000");
			out.write("CX:U:005000100000000");
			
			//Wait
			out.write("XX:X:S00010000000000");
		    
			//Send http connection
			out.write("XX:X:ACK000000000000");
			
		    out.flush();
		    
		    System.out.println("Completed");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new TestTriggerClient();
	}
}
