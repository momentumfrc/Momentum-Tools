package com.momentum4999.utils.controllers;

import com.momentum4999.utils.Utils;

import edu.wpi.first.wpilibj.GenericHID;

public class LogitechF310 extends GenericHID {
    /** Represents a digital button on an F310 Controller. */
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

    /** Represents an axis on an F310 Controller. */
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

    public double getLeftX() {
        return getRawAxis(Axis.kLeftX.value);
    }

    public double getRightX() {
        return getRawAxis(Axis.kRightX.value);
    }

    public double getLeftY() {
        return getRawAxis(Axis.kLeftY.value);
    }

    public double getRightY() {
        return getRawAxis(Axis.kRightY.value);
    }

    // TODO: Validate and document getLeftTriggerAxis() and getRightTriggerAxis()
    //       The implementations of these methods were copied directly from the old getTriggerAxis()
    //       method, so I'm not 100% certain about their implementation. However, it appears
    //       that the LogitechF310 just uses a single axis for both triggers, and just subtracts
    //       the left trigger from the right trigger (this means that holding down both triggers
    //       would cause the same reading as not holding either trigger). I find this behavior
    //       surprising and unexpected, and if it is truly the case, we should document it in
    //       this class' javadoc.
    public double getLeftTriggerAxis() {
        return -1 * Utils.clip(getRawAxis(Axis.kTriggers.value), -1, 0);
    }

    public double getRightTriggerAxis() {
        return Utils.clip(getRawAxis(Axis.kTriggers.value), 0, 1);
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

    public boolean getLeftBumper() {
        return getRawButton(Button.kBumperLeft.value);
    }
    public boolean getLeftBumperPressed() {
        return getRawButtonPressed(Button.kBumperLeft.value);
    }
    public boolean getLeftBumperReleased() {
        return getRawButtonReleased(Button.kBumperLeft.value);
    }

    public boolean getRightBumper() {
        return getRawButton(Button.kBumperRight.value);
    }
    public boolean getRightBumperPressed() {
        return getRawButtonPressed(Button.kBumperRight.value);
    }
    public boolean getRightBumperReleased() {
        return getRawButtonReleased(Button.kBumperRight.value);
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

    // TODO: Add wrappers for the stick buttons
    //       I'm not certain if the F310 has buttons when pushing in the sticks, but if it does,
    //       we should support them.
}
