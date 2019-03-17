package org.usfirst.frc.team4999.pid;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public abstract class MomentumPIDBase implements Sendable {
	private double kP, kI, kD, kF, iErrZone;
	private double targetZone, targetTime;
	private double setpoint = 0;
	private String name, subsystem = "Ungrouped";

	private long lastTime;
	private boolean enabled;
	
    private PIDConstantUpdateListener updateListener = ()->{};
    
    protected long getTimeMillis() {
        return System.currentTimeMillis();
    }
	
	public MomentumPIDBase(String name, double kP, double kI, double kD, double kF, double iErrZone, double targetZone, double targetTime) {
		this.name = name;
		this.iErrZone = iErrZone;
		this.targetZone = targetZone;
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kF = kF;
        this.targetTime = targetTime;
        lastTime = getTimeMillis();
	}
	
	public double getSetpoint() {
		return setpoint;
	}
	public void setSetpoint(double setpoint) {
		this.setpoint = setpoint;
	}
	
	public boolean onTarget() {
		return Math.abs(setpoint - getCurrentInput()) < targetZone;
	}
	
	public boolean onTargetForTime() {
		if(onTarget() && getTimeMillis() - lastTime > (targetTime * 1000)) {
			return true;
		} else if (!onTarget()) {
			lastTime = getTimeMillis();
		}
		return false;
	}
	
	public void setTargetTime(double time) {
		targetTime = time;
		updateListener.update();
	}
	public double getTargetTime() {
		return targetTime;
	}
	
	public void enable() {
		enabled = true;
	}
	public void disable() {
		enabled = false;
	}
	public void setEnabled(boolean enabled) {
		if(enabled)
			enable();
		else
			disable();
	}
	public boolean isEnabled() {
		return enabled;
	}
	public double getP() {
		return kP;
	}
	public double getI() {
		return kI;
	}
	public double getD() {
		return kD;
	}
	public double getF() {
		return kF;
	}
	public double getErrZone() {
		return iErrZone;
	}
	public double getTargetZone() {
		return targetZone;
    }
    
    abstract public double getCurrentInput();
    abstract public double getOutput();
	
	public void setP(double p) {
		kP = p;
		updateListener.update();
	}
	public void setI(double i) {
		kI = i;
		updateListener.update();
	}
	public void setD(double d) {
		kD = d;
		updateListener.update();
	}
	public void setF(double f) {
		kF = f;
		updateListener.update();
	}
	public void setErrZone(double zone) {
		iErrZone = zone;
		updateListener.update();
	}
	public void setTargetZone(double zone) {
		targetZone = zone;
		updateListener.update();
	}
	
	public void setListener(PIDConstantUpdateListener listener) {
		updateListener = listener;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getSubsystem() {
		return subsystem;
	}

	@Override
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;		
	}


	@Override
	public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType("MomentumPIDController");
		builder.addDoubleProperty("p", this::getP, this::setP);
		builder.addDoubleProperty("i", this::getI, this::setI);
		builder.addDoubleProperty("d", this::getD, this::setD);
		builder.addDoubleProperty("f", this::getF, this::setF); 
		builder.addDoubleProperty("errZone", this::getErrZone, this::setErrZone);
		builder.addDoubleProperty("targetZone", this::getTargetZone, this::setTargetZone);
		builder.addDoubleProperty("targetTime", this::getTargetTime, this::setTargetTime);
		builder.addDoubleProperty("setpoint", this::getSetpoint, this::setSetpoint);
		builder.addBooleanProperty("enabled", this::isEnabled, this::setEnabled);
		builder.addDoubleProperty("current", this::getCurrentInput, null);
		builder.addDoubleProperty("output", this::getOutput, null);
	}
	

}
