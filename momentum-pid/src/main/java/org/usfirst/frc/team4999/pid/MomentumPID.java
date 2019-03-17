package org.usfirst.frc.team4999.pid;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class MomentumPID extends MomentumPIDBase {
	private double result = 0;
	private PIDSource source;
	private PIDOutput output;
	
	private double totalErr;
	private double lastErr;
	private long lastTime;
	
	
	public MomentumPID(String name, double kP, double kI, double kD, double kF, double iErrZone, double targetZone, double targetTime, PIDSource input, PIDOutput output) {
		super(name, kP, kI, kD, kF, iErrZone, targetZone, targetTime);

		this.source = input;
		setSetpoint(source.pidGet());
		this.output = output;

		lastTime = getTimeMillis();
	}

	
	public void calculate() {
		// calculate time for dT
		long now = getTimeMillis();
		long dTime = now - lastTime;
		lastTime = now;
		
		// Calculate error
		double err = getSetpoint() - source.pidGet();
		
		// Calculate totalErr
		if(Math.abs(err) > getErrZone()) {
			totalErr = 0;
		} else {
			totalErr += err * dTime;
		}
		
		// Calculate dErr
		double dErr = (err - lastErr) / dTime;
		lastErr = err;
		
		// Combine all the parts
		// kF * result is feedforward used for velocity PID
		// kF is usually 0 or 1
		result = getF() * result + getP() * err + getI() * totalErr + getD() * dErr;
		
		// Write the result
		if(output != null)
			output.pidWrite(result);
	}
	
	public void zeroOutput() {
		result = 0;
		if(output != null)
			output.pidWrite(result);
	}
	
	/** 
	 * Sets the setpoint to a value relative to the current position 
	 * @param delta The difference between the new setpoint and the current position 
	 */ 
	public void setSetpointRelative(double delta) { 
		setSetpoint(source.pidGet() + delta); 
	}
	
	
	/**
	 * Gets the most recent result of the PID calculation
	 * @return The most recent result of the PID calculation
	 */
	@Override
	public double getOutput() {
		return result;
	}

	@Override
	public double getCurrentInput() {
		return this.source.pidGet();
	}

}
