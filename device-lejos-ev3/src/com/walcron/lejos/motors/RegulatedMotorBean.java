package com.walcron.lejos.motors;

import java.util.Optional;

import lejos.hardware.motor.BaseRegulatedMotor;

public class RegulatedMotorBean {

	private Optional<BaseRegulatedMotor> regulatedMotor1;
	private Optional<BaseRegulatedMotor> regulatedMotor2;
	
	public RegulatedMotorBean(Optional<BaseRegulatedMotor> regulatedMotor1, Optional<BaseRegulatedMotor> regulatedMotor2) {
		this.regulatedMotor1 = regulatedMotor1;
		this.regulatedMotor2 = regulatedMotor2;
	}
	
	public Optional<BaseRegulatedMotor> getRegulatedMotor1() {
		return regulatedMotor1;
	}
	
	public Optional<BaseRegulatedMotor> getRegulatedMotor2() {
		return regulatedMotor2;
	}
	
	public void clearRegulatedMotor2() {
		this.regulatedMotor2 = Optional.empty();
	}

}
