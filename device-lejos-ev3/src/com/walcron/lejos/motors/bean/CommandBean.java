package com.walcron.lejos.motors.bean;

public class CommandBean {

	private final String port;
	private final String motor;
	private final String action;
	
	public CommandBean(String port, String motor, String action) {
		this.port = port;
		this.motor = motor;
		this.action = action;
	}

	public String getPort() {
		return port;
	}

	public String getMotor() {
		return motor;
	}

	public String getAction() {
		return action;
	}

}
