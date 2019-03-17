package org.usfirst.frc.team4999.pid;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

public class SendableCANPIDController extends MomentumPIDBase {

    private CANSparkMax max;
    private CANPIDController controller;
    private CANEncoder encoder;
    

    private double orig_kP, orig_kI, orig_kD, orig_kF, orig_errZone;
    
    private ControlType controltype = ControlType.kPosition;

    private boolean saveOnEnable;

    public SendableCANPIDController(String name, double kP, double kI, double kD, double kF, double iErrZone, double targetZone, double targetTime, boolean saveOnEnable, CANSparkMax max) {

        super(name, kP, kI, kD, kF, iErrZone, targetZone, targetTime);

        this.max = max;
        this.controller = max.getPIDController();
        this.encoder = max.getEncoder();

        orig_kP = controller.getP();
        orig_kI = controller.getI();
        orig_kD = controller.getD();
        orig_kF = controller.getFF();
        orig_errZone = controller.getIZone();

        this.saveOnEnable = saveOnEnable;
    }

    public ControlType getControlType() {
        return controltype;
    }

    @Override
    public double getCurrentInput() {
        switch(controltype) {
            case kPosition:
                return encoder.getPosition();
            case kVelocity:
                return encoder.getVelocity();
            case kDutyCycle:
                // NOTE: I think this is right?
                return max.getAppliedOutput();
            case kVoltage:
                // NOTE: This is untested, and probably wrong
                // REV doesn't document very well what kVoltage control is, so I'm not sure what to put here
                return max.getBusVoltage();
            default:
                assert false : "Tried to use unsupported ControlType";
                return 0;
        }
    }

    @Override
    public double getOutput() {
        return this.max.get();
    }
    
    @Override
    public void setP(double p) {
        super.setP(p);
        if(isEnabled()) {
            controller.setP(p);
        }
    }

    @Override
    public void setI(double i) {
        super.setI(i);
        if(isEnabled()) {
            controller.setI(i);
        }
    }

    @Override
    public void setD(double d) {
        super.setD(d);
        if(isEnabled()) {
            controller.setD(d); 
        }
    }

    @Override
    public void setF(double f) {
        super.setF(f);
        if(isEnabled()) {
            controller.setFF(f);
        }
    }

    public void setErrZone(double errZone) {
        super.setErrZone(errZone);
        if(isEnabled()) {
            controller.setIZone(errZone);
        }
    }

    @Override
    public void setSetpoint(double setpoint) {
        super.setSetpoint(setpoint);
        controller.setReference(setpoint, controltype);
    }

    public void setControlType(ControlType controltype) {
        this.controltype = controltype;
        controller.setReference(getSetpoint(), controltype);
    }

    @Override
    public void enable() {
        super.enable();
        setSetpoint(getSetpoint());
        if(saveOnEnable)
            saveValues();
    }

    public void disable() {
        super.disable();
        max.stopMotor();
    }

    public void saveValues() {
        if( getP() != orig_kP
            || getI() != orig_kI
            || getD() != orig_kD
            || getF() != orig_kF
            || getErrZone() != orig_errZone ) {
                max.burnFlash();
            }
        orig_kP = getP();
        orig_kI = getI();
        orig_kD = getD();
        orig_kF = getF();
        orig_errZone = getErrZone();
    }

}