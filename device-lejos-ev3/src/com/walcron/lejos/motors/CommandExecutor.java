package com.walcron.lejos.motors;

import java.util.Optional;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

public class CommandExecutor extends Thread {

	public static final int LENGTH_OF_COMMAND = 16; //i.e. "AX:L:03601000100"
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
			
			PortsBean ports = getPort(translatedCommand.getPort());
			if(ports.getPortOne().isPresent() || ports.getPortTwo().isPresent()) {
				System.out.println("Running on port:"+splittedCommand[0]);
				
				RegulatedMotorBean regulatedMotors = getMotorType(translatedCommand.getMotor(), ports);
				
				if(regulatedMotors.getRegulatedMotor1().isPresent() || regulatedMotors.getRegulatedMotor2().isPresent()) {
					System.out.println("Running on motor:"+splittedCommand[1]);
					doAction(translatedCommand.getAction(), regulatedMotors);
				}
			}
		}
		else {
			
		}
	}
	
	private void doAction(String action, RegulatedMotorBean regulatedMotors) {
		//first 4, -360 to 0360
		int rotation = Integer.parseInt(action.substring(0, 4), 10);
		//second 4, 0000 to 6000
		int acceleration = Integer.parseInt(action.substring(4, 8), 10);
		//third 3, 000 to 900
		int speed = Integer.parseInt(action.substring(8, 11), 10);
		boolean synch = (regulatedMotors.getRegulatedMotor1().isPresent() && regulatedMotors.getRegulatedMotor2().isPresent());
		
		System.out.println(String.format("ro:%d,ac:%d,sp:%d,bt:%b", rotation, acceleration, speed, synch));
		
		if(synch) {
			regulatedMotors.getRegulatedMotor1().get().startSynchronization();
			regulatedMotors.getRegulatedMotor2().get().startSynchronization();
		}
		
		if(regulatedMotors.getRegulatedMotor1().isPresent()) {
			BaseRegulatedMotor motor1 = regulatedMotors.getRegulatedMotor1().get();
			motor1.setAcceleration(acceleration);
			motor1.setSpeed(speed);
			motor1.rotate(rotation, true);
		}
		
		if(regulatedMotors.getRegulatedMotor2().isPresent()) {
			BaseRegulatedMotor motor2 = regulatedMotors.getRegulatedMotor2().get();
			motor2.setAcceleration(acceleration);
			motor2.setSpeed(speed);
			motor2.rotate(rotation, true);
		}
		
		if(synch) {
			regulatedMotors.getRegulatedMotor1().get().endSynchronization();
			regulatedMotors.getRegulatedMotor2().get().endSynchronization();
			
			
		}
		
		if(regulatedMotors.getRegulatedMotor1().isPresent()) {
			regulatedMotors.getRegulatedMotor1().get().waitComplete();
			regulatedMotors.getRegulatedMotor1().get().close();
		}
		
		if(regulatedMotors.getRegulatedMotor2().isPresent()) {
			regulatedMotors.getRegulatedMotor2().get().waitComplete();
			regulatedMotors.getRegulatedMotor2().get().close();
		}
		
	}

	private RegulatedMotorBean getMotorType(String motorType, PortsBean portsBean) {
		switch (motorType) {
			case "L":
				return new RegulatedMotorBean(
						portsBean.getPortOne().isPresent()?
							Optional.of(new EV3LargeRegulatedMotor(portsBean.getPortOne().get())): Optional.empty(),
						portsBean.getPortTwo().isPresent()?
							Optional.of(new EV3LargeRegulatedMotor(portsBean.getPortTwo().get())): Optional.empty()
						);
			case "M":
				return new RegulatedMotorBean(
						portsBean.getPortOne().isPresent()?
							Optional.of(new EV3MediumRegulatedMotor(portsBean.getPortOne().get())): Optional.empty(),
						Optional.empty()
						);
			default:
				return new RegulatedMotorBean(Optional.empty(), Optional.empty());
		}
	}

	public PortsBean getPort(String port) {
		
		switch(port) {
			case "AX":
				return new PortsBean(Optional.of(MotorPort.A), Optional.empty());
			case "BX":
				return new PortsBean(Optional.of(MotorPort.B), Optional.empty());
			case "CX":
				return new PortsBean(Optional.of(MotorPort.C), Optional.empty());
			case "DX":
				return new PortsBean(Optional.of(MotorPort.D), Optional.empty());
			case "AB":
				return new PortsBean(Optional.of(MotorPort.A), Optional.of(MotorPort.B));
			case "AC":
				return new PortsBean(Optional.of(MotorPort.A), Optional.of(MotorPort.C));
			case "AD":
				return new PortsBean(Optional.of(MotorPort.A), Optional.of(MotorPort.D));
			case "BC":
				return new PortsBean(Optional.of(MotorPort.B), Optional.of(MotorPort.C));
			case "BD":
				return new PortsBean(Optional.of(MotorPort.B), Optional.of(MotorPort.D));
			case "CD":
				return new PortsBean(Optional.of(MotorPort.C), Optional.of(MotorPort.D));
			default:
				return new PortsBean(Optional.empty(), Optional.empty());
		}
	}

}
