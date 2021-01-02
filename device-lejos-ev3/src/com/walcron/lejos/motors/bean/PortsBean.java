package com.walcron.lejos.motors.bean;

import java.util.Optional;

import lejos.hardware.port.Port;

public class PortsBean {

	private final Optional<Port> portOne;
	private final Optional<Port> portTwo;
	
	public PortsBean(Optional<Port> portOne, Optional<Port> portTwo) {
		this.portOne = portOne;
		this.portTwo = portTwo;
	}

	public Optional<Port> getPortOne() {
		return portOne;
	}
	
	public Optional<Port> getPortTwo() {
		return portTwo;
	}
}
