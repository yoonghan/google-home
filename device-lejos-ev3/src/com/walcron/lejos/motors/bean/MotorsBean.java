package com.walcron.lejos.motors.bean;

import java.util.Optional;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;

public class MotorsBean {

	private Optional<UnregulatedMotor> unregulatedMotor1;
	private Optional<BaseRegulatedMotor> regulatedMotor1;
	private Optional<BaseRegulatedMotor> regulatedMotor2;
	
	public MotorsBean(
			Optional<BaseRegulatedMotor> regulatedMotor1, Optional<BaseRegulatedMotor> regulatedMotor2,
			Optional<UnregulatedMotor> unregulatedMotor1
			) {
		this.regulatedMotor1 = regulatedMotor1;
		this.regulatedMotor2 = regulatedMotor2;
		this.unregulatedMotor1 = unregulatedMotor1;
	}
	
	public Optional<BaseRegulatedMotor> getRegulatedMotor1() {
		return regulatedMotor1;
	}
	
	public Optional<BaseRegulatedMotor> getRegulatedMotor2() {
		return regulatedMotor2;
	}
	
	public Optional<UnregulatedMotor> getUnregulatedMotor1() {
		return unregulatedMotor1;
	}

	public boolean hasRegulatedMotorsToRun() {
		return regulatedMotor1.isPresent() || regulatedMotor2.isPresent();
	}
	
	public boolean hasUnregulatedMotorsToRun() {
		return unregulatedMotor1.isPresent();
	}
}
