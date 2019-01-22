package org.usfirst.frc.team4999.pid;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class SendableCANPIDController implements Sendable {

    private CANSparkMax max;
    private CANPIDController controller;
    private CANEncoder encoder;

    private double kP, kI, kD, kF;
    private double errZone;

    private double orig_kP, orig_kI, orig_kD, orig_kF, orig_errZone;
    private double targetZone, targetTime;
    private double setpoint;
    private boolean enabled = true;
    private ControlType controltype = ControlType.kPosition;
    private double time;

    private String name, subsystem = "Ungrouped";

    private PIDConstantUpdateListener updateListener = ()->{};

    public SendableCANPIDController(String name, CANSparkMax max, double targetZone, double targetTime) {
        this.name = name;
        this.max = max;
        this.controller = max.getPIDController();
        this.encoder = max.getEncoder();
        time = System.currentTimeMillis();

        kP = getP();
        kI = getI();
        kD = getD();
        kF = getF();
        errZone = getErrZone();

        orig_kP = controller.getP();
        orig_kI = controller.getI();
        orig_kD = controller.getD();
        orig_kF = controller.getFF();
        orig_errZone = controller.getIZone();

        this.targetZone = targetZone;
        this.targetTime = targetTime;
    }

    public void setListener(PIDConstantUpdateListener listener) {
        updateListener = listener;
    }

    public double getP() {
        if(enabled)
            return controller.getP();
        else
            return kP;
    }

    public double getI() {
        if(enabled)
            return controller.getI();
        else
            return kI;
    }

    public double getD() {
        if(enabled)
            return controller.getD();
        else
            return kD;
    }

    public double getF() {
        if(enabled)
            return controller.getFF();
        else
            return kF;
    }

    public double getErrZone() {
        if(enabled)
            return controller.getIZone();
        else
            return errZone;
    }

    public double getTargetZone() {
        return targetZone;
    }

    public double getTargetTime() {
        return targetTime;
    }

    public double getSetpoint() {
        return setpoint;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ControlType getControlType() {
        return controltype;
    }
    
    public void setP(double p) {
        kP = p;
        updateListener.update();
        if(enabled) {
            controller.setP(p);
        }
    }

    public void setI(double i) {
        kI = i;
        updateListener.update();
        if(enabled) {
            controller.setI(i);
        }
    }

    public void setD(double d) {
        kD = d;
        updateListener.update();
        if(enabled) {
            controller.setD(d); 
        }
    }

    public void setF(double f) {
        kF = f;
        updateListener.update();
        if(enabled) {
            controller.setFF(f);
        }
    }

    public void setErrZone(double errZone) {
        this.errZone = errZone;
        updateListener.update();
        if(enabled) {
            controller.setIZone(errZone);
        }
    }

    public void setTargetZone(double targetZone) {
        this.targetZone = targetZone;
        updateListener.update();
    }

    public void setTargetTime(double targetTime) {
        this.targetTime = targetTime;
        updateListener.update();
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
        controller.setReference(setpoint, controltype);
    }

    public void setControlType(ControlType controltype) {
        this.controltype = controltype;
        controller.setReference(setpoint, controltype);
        updateListener.update();
    }

    public void enable() {
        enabled = true;
        setP(kP);
        setI(kI);
        setD(kD);
        setF(kF);
        setErrZone(errZone);
        setSetpoint(setpoint);
        saveValues();
    }

    public void disable() {
        kP = getP();
        kI = getI();
        kD = getD();
        kF = getF();
        errZone = getErrZone();
        enabled = false;
        controller.setP(0);
        controller.setI(0);
        controller.setD(0);
        controller.setFF(0);
        controller.setIZone(0);
        controller.setReference(0, controltype);
    }

    public void setEnabled(boolean enable) {
        if (enable != enabled) {
            if(enable) {
                enable();
            } else {
                disable();
            }
        }
    }

    public boolean onTarget() {
        double value = 0;
        switch(controltype) {
            case kPosition:
                value = encoder.getPosition();
                break;
            case kVelocity:
                value = encoder.getVelocity();
                break;
            case kDutyCycle:
                // NOTE: I think this is right?
                value = max.getAppliedOutput();
                break;
            case kVoltage:
                // NOTE: This is untested, and probably wrong
                // REV doesn't document very well what kVoltage control is, so I'm not sure what to put here
                value = max.getBusVoltage();
                break;
        }
        return Math.abs(setpoint - value) < targetZone;
    }
    
    public boolean onTargetForTime() {
		if(onTarget() && System.currentTimeMillis() - time > (targetTime * 1000) ) {
			return true;
		} else if (!onTarget()) {
			time = System.currentTimeMillis();
		}
		return false;
    }

    public void saveValues() {
        if( kP != orig_kP
            || kI != orig_kI
            || kD != orig_kD
            || kF != orig_kF
            || errZone != orig_errZone ) {
                max.burnFlash();
            }
        orig_kP = kP;
        orig_kI = kI;
        orig_kD = kD;
        orig_kF = kF;
        orig_errZone = errZone;
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
	}

}