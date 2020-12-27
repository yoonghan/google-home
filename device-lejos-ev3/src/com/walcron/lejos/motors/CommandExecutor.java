package com.walcron.lejos.motors;

import java.util.Optional;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

public class CommandExecutor extends Thread {

	public static final int LENGTH_OF_COMMAND = 15; //i.e. "A:L:03601000100"
	private final long RUNNING_INSTANCE = 1000;
	private volatile boolean running = true;
	
	public CommandExecutor() {
	}
	
	public void run() {
		while(running) {
			Optional<String> _command = CommandMailbox.INSTANCE.getCommand();
			if(_command.isPresent()) {
				String command = _command.get();
				System.out.println("Executing:"+command);
				translateToMotorCall(command);
			}
			
			try {
				Thread.sleep(RUNNING_INSTANCE);
			}
			catch(InterruptedException ie) {
				this.running = false;
				ie.printStackTrace();
			}
		}
	}
	
	public void translateToMotorCall(String command) {
		String[] splittedCommand = command.split(":");
		
		if(splittedCommand.length == 3 && command.length() == LENGTH_OF_COMMAND) {
			System.out.println("Executing");
			CommandBean translatedCommand = new CommandBean(splittedCommand[0], splittedCommand[1], splittedCommand[2]);
			
			Optional<Port> _port = getPort(translatedCommand.getPort());
			if(_port.isPresent()) {
				System.out.println("Running on port:"+splittedCommand[0]);
				Optional<BaseRegulatedMotor> _baseRegulatedMotor = getMotorType(translatedCommand.getMotor(), _port.get());
				
				if(_baseRegulatedMotor.isPresent()) {
					System.out.println("Running on motor:"+splittedCommand[1]);
					doAction(translatedCommand.getAction(), _baseRegulatedMotor.get());
				}
			}
		}
		else {
			
		}
	}
	
	private void doAction(String action, BaseRegulatedMotor baseRegulatedMotor) {
		//first 4, -360 to 0360
		int rotation = Integer.parseInt(action.substring(0, 4), 10);
		//second 4, 0000 to 6000
		int acceleration = Integer.parseInt(action.substring(4, 8), 10);
		//third 3, 000 to 100
		int speed = Integer.parseInt(action.substring(8, 11), 10);
		
		System.out.println(String.format("r:%d,a:%d,s:%d", rotation, acceleration, speed));
		
		baseRegulatedMotor.rotate(rotation);
		baseRegulatedMotor.setAcceleration(acceleration);
		baseRegulatedMotor.setSpeed(speed);
		
		baseRegulatedMotor.close();
		
	}

	private Optional<BaseRegulatedMotor> getMotorType(String motorType, Port port) {
		switch (motorType) {
			case "L":
				return Optional.of(new EV3LargeRegulatedMotor(port));
			case "M":
				return Optional.of(new EV3MediumRegulatedMotor(port));
			default:
				return Optional.empty();
		}
	}

	public Optional<Port> getPort(String port) {
		
		switch(port) {
			case "A":
				return Optional.of(MotorPort.A);
			case "B":
				return Optional.of(MotorPort.B);
			case "C":
				return Optional.of(MotorPort.C);
			case "D":
				return Optional.of(MotorPort.D);
			default:
				return Optional.empty();
		}
	}

}
