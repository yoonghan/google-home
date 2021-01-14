package com.walcron.lejos.command;

import java.util.Optional;

import com.walcron.lejos.api.Const;
import com.walcron.lejos.api.JsonHttpClient;
import com.walcron.lejos.motors.bean.CommandBean;
import com.walcron.lejos.motors.bean.PortsBean;
import com.walcron.lejos.motors.bean.MotorsBean;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class CommandExecutor extends Thread {

	public static final int LENGTH_OF_COMMAND = 20; //i.e. "AX:L:036000001000100"
	private final long RUNNING_INSTANCE = 1000;
	private volatile boolean running = true;
	private final JsonHttpClient jsonHttpClient;
	
	public CommandExecutor() {
		this.jsonHttpClient = new JsonHttpClient();
	}
	
	public void run() {
		while(running) {
			Optional<String> _command = CommandMailbox.INSTANCE.getCommand();
			if(_command.isPresent()) {
				String command = _command.get();
				System.out.println("Executing:"+command);
				translateToCommandCall(command);
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
	
	public void translateToCommandCall(String command) {
		String[] splittedCommand = command.split(":");
		
		if(splittedCommand.length == 3 && 
				(command.length() == LENGTH_OF_COMMAND)) {
			CommandBean translatedCommand = new CommandBean(splittedCommand[0], splittedCommand[1], splittedCommand[2]);
			
			PortsBean ports = getPort(translatedCommand.getPort());
			if(ports.getPortOne().isPresent() || ports.getPortTwo().isPresent()) {
				System.out.println("port:"+splittedCommand[0]);
				
				MotorsBean motors = getMotorType(translatedCommand.getMotor(), ports);
				
				if(motors.hasRegulatedMotorsToRun()) {
					System.out.println("r-motor:"+splittedCommand[1]);
					doRegulatedMotorAction(translatedCommand.getAction(), motors);
				}
				
				if(motors.hasUnregulatedMotorsToRun()) {
					System.out.println("u-motor:"+splittedCommand[1]);
					doUnregulatedMotorAction(translatedCommand.getAction(), motors);
				}
				
				doMotorClose(motors);
			}
			else {
				doNonMotorCommand(translatedCommand);
			}
		}
		else {
			System.err.println("Check length");
		}
	}
	
	private void doNonMotorCommand(CommandBean translatedCommand) {
		String action = translatedCommand.getAction(); 
		
		if(action.startsWith("S")) {
			long waitTime = Long.parseLong(action.substring(1, 7));
			System.out.println("WAIT:"+waitTime);
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("DONE");
		}
		
		if(action.startsWith("ACK")) {
			String message = action.substring(3);
			System.out.println("ACK-"+Const.URL);
			System.out.println("MSG-"+message);
			jsonHttpClient.sendAck(message);
		}
	}

	private void doUnregulatedMotorAction(String action, MotorsBean motors) {
		//first 4, -100 to 0100
		int power = Integer.parseInt(action.substring(0, 4), 10);
		//second 2, 0 to 999 seconds delay
		int delay = Integer.parseInt(action.substring(4, 7), 10);
		//rest are dumped...remember socket set to LENGTH_OF_COMMAND
		System.out.println(String.format("p:%d,d:%d", power, delay));		
		if(motors.getUnregulatedMotor1().isPresent()) {
			executeUnregulatedMotorAction(motors.getUnregulatedMotor1().get(), power, delay);
		}
	}
	
	private void doRegulatedMotorAction(String action, MotorsBean motors) {
		//first 4, -360 to 0360
		int rotation1 = Integer.parseInt(action.substring(0, 4), 10);
		//second 4, -360 to 0360
		int rotation2 = Integer.parseInt(action.substring(4, 8), 10);
		//third 4, 0000 to 6000
		int acceleration = Integer.parseInt(action.substring(8, 12), 10);
		//fourth 3, 000 to 900
		int speed = Integer.parseInt(action.substring(12, 15), 10);
		
		boolean synch = (
				motors.getRegulatedMotor1().isPresent() && motors.getRegulatedMotor2().isPresent() &&
				rotation1 == rotation2
				);
		
		System.out.println(String.format("r1:%d,r2:%d,a:%d,s:%d,2:%b", rotation1, rotation2, acceleration, speed, synch));
		
		if(synch) {
			motors.getRegulatedMotor1().get().startSynchronization();
			motors.getRegulatedMotor2().get().startSynchronization();
		}
		
		if(motors.getRegulatedMotor1().isPresent()) {
			executeRegulatedMotorAction(motors.getRegulatedMotor1().get(), acceleration, speed, rotation1);
		}
		
		if(motors.getRegulatedMotor2().isPresent()) {
			executeRegulatedMotorAction(motors.getRegulatedMotor2().get(), acceleration, speed, rotation2);
		}
		
		if(synch) {
			motors.getRegulatedMotor1().get().endSynchronization();
			motors.getRegulatedMotor2().get().endSynchronization();
		}
	}
	
	private void executeRegulatedMotorAction(BaseRegulatedMotor baseRegulatedMotor, int acceleration, int speed,
			int rotation) {
		if(acceleration > 0)
			baseRegulatedMotor.setAcceleration(acceleration);
		if(speed > 0)
			baseRegulatedMotor.setSpeed(speed);
		
		baseRegulatedMotor.rotate(rotation, true);
	}

	private void executeUnregulatedMotorAction(UnregulatedMotor unregulatedMotor, int power, int delay) {
		if(power > 100 ||  power < -100) {
			System.err.println("Unregulated in 100th range");
		}
		else {
			unregulatedMotor.setPower(Math.abs(power));
			
			if(power < 0) {
				unregulatedMotor.backward();
			}
			
			if(power > 0) {
				unregulatedMotor.forward();
			}
			
			Delay.msDelay(delay * 1000L);
			
			unregulatedMotor.stop();
		}
	}

	private void doMotorClose(MotorsBean motors) {
		if(motors.getRegulatedMotor1().isPresent()) {
			motors.getRegulatedMotor1().get().waitComplete();
			motors.getRegulatedMotor1().get().close();
		}
		
		if(motors.getRegulatedMotor2().isPresent()) {
			motors.getRegulatedMotor2().get().waitComplete();
			motors.getRegulatedMotor2().get().close();
		}
		
		if(motors.getUnregulatedMotor1().isPresent()) {
			motors.getUnregulatedMotor1().get().close();
		}
	}

	private MotorsBean getMotorType(String motorType, PortsBean portsBean) {
		switch (motorType) {
			case "L":
				return new MotorsBean(
						portsBean.getPortOne().isPresent()?
							Optional.of(new EV3LargeRegulatedMotor(portsBean.getPortOne().get())): Optional.empty(),
						portsBean.getPortTwo().isPresent()?
							Optional.of(new EV3LargeRegulatedMotor(portsBean.getPortTwo().get())): Optional.empty(),
						Optional.empty()
						);
			case "M":
				return new MotorsBean(
						portsBean.getPortOne().isPresent()?
							Optional.of(new EV3MediumRegulatedMotor(portsBean.getPortOne().get())): Optional.empty(),
						Optional.empty(),
						Optional.empty()
						);
			case "U":
				return new MotorsBean(
						Optional.empty(),
						Optional.empty(),
						portsBean.getPortOne().isPresent()? 
								Optional.of(new UnregulatedMotor(portsBean.getPortOne().get())): Optional.empty()
						);
			default:
				return new MotorsBean(Optional.empty(), Optional.empty(), Optional.empty());
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
