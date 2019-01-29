package org.usfirst.frc.team4999.pid;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;

public class PathFollower implements MotionController {

    private double kV, kA, kP, kD;
    private PIDOutput out;
    private PIDSource in;

    private Path path;
    private Path nextPath;

    private double result = 0;

    private double lastt = 0;
    private double startt = 0;

    private double lasterr = 0;

    private boolean enabled = true;

    public PathFollower(double kV, double kA, double kP, double kD, PIDOutput out, PIDSource in) {
        this.kV = kV;
        this.kA = kA;
        this.kP = kP;
        this.kD = kD;
        this.out = out;
        this.in = in;
    }

    public void setPath(Path path) {
        this.nextPath = path;
    }

    public void startPath() {
        path = nextPath;
        lastt = getTimeMillis();
        startt = getTimeMillis();
    }

    private double getTimeMillis() {
		return Timer.getFPGATimestamp() * 1000;
	}

    @Override
    public void calculate() {
        if(!enabled || path == null) {
            result = 0;
        } else {
            double time = startt - getTimeMillis();
            
            double[] setp = path.getAtT(time);
            double set_pos = setp[0];
            double set_vel = setp[1];
            double set_acc = setp[2];

            double error = set_pos - in.pidGet();

            double dtime = lastt - getTimeMillis();
            lastt = getTimeMillis();

            double derr = (error - lasterr) / dtime;
            lasterr = error;

            double ff_term = kV * set_vel + kA * set_acc;
            double fb_term = kP * error + kD * derr;

            result = ff_term + fb_term;
        }
        if(out != null) {
            out.pidWrite(result);
        }
    }

    @Override
    public double get() {
        return result;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    @Override
    public void zeroOutput() {
        if(out != null) {
            out.pidWrite(0);
        }
    }
}