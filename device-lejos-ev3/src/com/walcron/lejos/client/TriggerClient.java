package com.walcron.lejos.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TriggerClient {

	private final int PORT = 9000;
	private final String HOST_NAME = "10.0.1.1";
	
	public TriggerClient() {
		try (
		    Socket clientSocket = new Socket(HOST_NAME, PORT);
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		){
			clientSocket.setKeepAlive(false);
			clientSocket.setSoTimeout(30_000);
			
		    out.write("AX:L:03602000900");
		    //out.write("\n");
		    out.write("BX:L:-3602000900");
		    //out.write("\n");
		    out.write("CX:M:03602000900");
		    //out.write("\n");
		    out.write("AB:L:17202000900");
		    out.flush();
		    
		    System.out.println("Completed");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new TriggerClient();
	}
}
