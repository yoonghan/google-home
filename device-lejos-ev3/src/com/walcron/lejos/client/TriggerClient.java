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
		    out.write("A:L:03601000100");
		    //out.write("\n");
		    out.write("B:L:-3600010020");
		    //out.write("\n");
		    out.write("C:M:03602000100");
		    //out.write("\n");
		    out.write("B:L:-3600010020");
		    out.write("B:L:-3600010020");
		    out.write("B:L:-3600010020");
		    out.write("B:L:-3600010020");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new TriggerClient();
	}
}
