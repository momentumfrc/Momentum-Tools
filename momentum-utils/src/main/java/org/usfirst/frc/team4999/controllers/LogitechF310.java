package org.usfirst.frc.team4999.controllers;

import org.usfirst.frc.team4999.utils.Utils;

import edu.wpi.first.wpilibj.GenericHID;

public class LogitechF310 extends GenericHID {
	/**
	 * Represents a digital button on an F310 Controller.
	*/
	public enum Button {
		kBumperLeft(5),
		kBumperRight(6),
		kA(1),
		kB(2),
		kX(3),
		kY(4),
		kBack(7),
		kStart(8);

		public final int value;

		Button(int value) {
			this.value = value;
		}
	}

	/**
	 * Represents an axis on an XboxController.
	 */
	public enum Axis {
		kLeftX(2),
		kRightX(4),
		kLeftY(1),
		kRightY(5),
		kTriggers(3);

		public final int value;

		Axis(int value) {
			this.value = value;
		}
	}
	
	public LogitechF310(int port) {
		super(port);
	}

	@Override
	public double getX(Hand hand) {
		if(hand == Hand.kLeft) {
			return getRawAxis(Axis.kLeftX.value);
		} else {
			return getRawAxis(Axis.kRightX.value);
		}
	}

	@Override
	public double getY(Hand hand) {
		if(hand == Hand.kLeft) {
			return getRawAxis(Axis.kLeftY.value);
		} else {
			return getRawAxis(Axis.kRightY.value);
		}
	}
	
	public double getTriggerAxis(Hand hand) {
		if(hand == Hand.kLeft) {
			return -Utils.clip(getRawAxis(Axis.kTriggers.value), -1, 0);
		} else {
			return Utils.clip(getRawAxis(Axis.kTriggers.value), 0, 1);
		}
	}
	
	public boolean getXButton() {
		return getRawButton(Button.kX.value);
	}
	public boolean getXButtonPressed() {
		return getRawButtonPressed(Button.kX.value);
	}
	public boolean getXButtonReleased() {
		return getRawButtonReleased(Button.kX.value);
	}
	
	public boolean getYButton() {
		return getRawButton(Button.kY.value);
	}
	public boolean getYButtonPressed() {
		return getRawButtonPressed(Button.kY.value);
	}
	public boolean getYButtonReleased() {
		return getRawButtonReleased(Button.kY.value);
	}
	
	public boolean getAButton() {
		return getRawButton(Button.kA.value);
	}
	public boolean getAButtonPressed() {
		return getRawButtonPressed(Button.kA.value);
	}
	public boolean getAButtonReleased() {
		return getRawButtonReleased(Button.kA.value);
	}

	public boolean getBButton() {
		return getRawButton(Button.kB.value);
	}
	public boolean getBButtonPressed() {
		return getRawButtonPressed(Button.kB.value);
	}
	public boolean getBButtonReleased() {
		return getRawButtonReleased(Button.kB.value);
	}
	
	public boolean getBumper(Hand hand) {
		if(hand == Hand.kLeft) {
			return getRawButton(Button.kBumperLeft.value);
		} else {
			return getRawButton(Button.kBumperRight.value);
		}
	}
	public boolean getBumperPressed(Hand hand) {
		if(hand == Hand.kLeft) {
			return getRawButtonPressed(Button.kBumperLeft.value);
		} else {
			return getRawButtonPressed(Button.kBumperRight.value);
		}
	}
	public boolean getBumperReleased(Hand hand) {
		if(hand == Hand.kLeft) {
			return getRawButtonReleased(Button.kBumperLeft.value);
		} else {
			return getRawButtonReleased(Button.kBumperRight.value);
		}
	}
	
	public boolean getStartButton() {
		return getRawButton(Button.kStart.value);
	}
	public boolean getStartButtonPressed() {
		return getRawButtonPressed(Button.kStart.value);
	}
	public boolean getStartButtonReleased() {
		return getRawButtonReleased(Button.kStart.value);
	}
	
	public boolean getBackButton() {
		return getRawButton(Button.kBack.value);
	}
	public boolean getBackButtonPressed() {
		return getRawButtonPressed(Button.kBack.value);
	}
	public boolean getBackButtonReleased() {
		return getRawButtonReleased(Button.kBack.value);
	}
	

}
