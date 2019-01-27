package org.usfirst.frc.team4999.controllers;

import org.usfirst.frc.team4999.utils.Utils;

import edu.wpi.first.wpilibj.GenericHID;

public class LogitechF310 extends GenericHID {
	private static final int X_BUTTON = 3;
	private static final int Y_BUTTON = 4;
	private static final int A_BUTTON = 1;
	private static final int B_BUTTON = 2;
	private static final int L_BUMPER = 5;
	private static final int R_BUMPER = 6;
	private static final int START = 8;
	private static final int BACK = 7;
	
	public LogitechF310(int port) {
		super(port);
	}

	@Override
	public double getX(Hand hand) {
		if(hand == Hand.kLeft) {
			return getRawAxis(2);
		} else {
			return getRawAxis(4);
		}
	}

	@Override
	public double getY(Hand hand) {
		if(hand == Hand.kLeft) {
			return getRawAxis(1);
		} else {
			return getRawAxis(5);
		}
	}
	
	public double getTriggerAxis(Hand hand) {
		if(hand == Hand.kLeft) {
			return -Utils.clip(getRawAxis(3), -1, 0);
		} else {
			return Utils.clip(getRawAxis(3), 0, 1);
		}
	}
	
	public boolean getXButton() {
		return getRawButton(X_BUTTON);
	}
	public boolean getXButtonPressed() {
		return getRawButtonPressed(X_BUTTON);
	}
	public boolean getXButtonReleased() {
		return getRawButtonReleased(X_BUTTON);
	}
	
	public boolean getYButton() {
		return getRawButton(Y_BUTTON);
	}
	public boolean getYButtonPressed() {
		return getRawButtonPressed(Y_BUTTON);
	}
	public boolean getYButtonReleased() {
		return getRawButtonReleased(Y_BUTTON);
	}
	
	public boolean getAButton() {
		return getRawButton(A_BUTTON);
	}
	public boolean getAButtonPressed() {
		return getRawButtonPressed(A_BUTTON);
	}
	public boolean getAButtonReleased() {
		return getRawButtonReleased(A_BUTTON);
	}

	public boolean getBButton() {
		return getRawButton(B_BUTTON);
	}
	public boolean getBButtonPressed() {
		return getRawButtonPressed(B_BUTTON);
	}
	public boolean getBButtonReleased() {
		return getRawButtonReleased(B_BUTTON);
	}
	
	public boolean getBumper(Hand hand) {
		if(hand == Hand.kLeft) {
			return getRawButton(L_BUMPER);
		} else {
			return getRawButton(R_BUMPER);
		}
	}
	public boolean getBumperPressed(Hand hand) {
		if(hand == Hand.kLeft) {
			return getRawButtonPressed(L_BUMPER);
		} else {
			return getRawButtonPressed(R_BUMPER);
		}
	}
	public boolean getBumperReleased(Hand hand) {
		if(hand == Hand.kLeft) {
			return getRawButtonReleased(L_BUMPER);
		} else {
			return getRawButtonReleased(R_BUMPER);
		}
	}
	
	public boolean getStartButton() {
		return getRawButton(START);
	}
	public boolean getStartButtonPressed() {
		return getRawButtonPressed(START);
	}
	public boolean getStartButtonReleased() {
		return getRawButtonReleased(START);
	}
	
	public boolean getBackButton() {
		return getRawButton(BACK);
	}
	public boolean getBackButtonPressed() {
		return getRawButtonPressed(BACK);
	}
	public boolean getBackButtonReleased() {
		return getRawButtonReleased(BACK);
	}
	

}
