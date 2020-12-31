package com.walcron.lejos.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.walcron.lejos.motors.CommandExecutor;
import com.walcron.lejos.motors.CommandMailbox;

public class LocalServer extends Thread {

	private final int portNumber;
	private volatile boolean running = true;
	
	public LocalServer(int portNumber) {
		super();
		this.portNumber = portNumber;
	}

	public void run() {

		try(ServerSocket serverSocket = new ServerSocket(portNumber)){
			while(running) {
				try (
				    final Socket clientSocket = serverSocket.accept();
				) {
					Runnable clientListener = () -> {
						try (
								final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						){
							char[] cbuf = new char[CommandExecutor.LENGTH_OF_COMMAND];
							while (in.read(cbuf) != -1) {
								CommandMailbox.INSTANCE.addCommand(String.valueOf(cbuf));
								System.out.println("Push to Queue");
								try {
									Thread.sleep(500);
								} catch (InterruptedException ie) {
									System.err.println("Interrupted");
								}
							}
						}
						catch(IOException ioe) {

						}
					};

					new Thread(clientListener).start();
				}
				catch(IOException ioe) {
					ioe.printStackTrace();
					this.running = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
