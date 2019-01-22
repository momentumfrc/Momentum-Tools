package org.usfirst.frc.team4999;

import java.util.Map;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

import com.google.common.collect.ImmutableMap;

import java.util.Objects;

public final class MomentumPIDControllerData extends ComplexData<MomentumPIDControllerData> {
    private final double p;
    private final double i;
    private final double d;
    private final double f;
    private final double errZone;
    private final double targetZone;
    private final double targetTime;
    private final double setpoint;
    private final boolean enabled;

    public MomentumPIDControllerData(double p, double i, double d, double f, double errZone, double targetZone, double targetTime, double setpoint, boolean enabled) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;
        this.errZone = errZone;
        this.targetZone = targetZone;
        this.targetTime = targetTime;
        this.setpoint = setpoint;
        this.enabled = enabled;
    }

    /**
     * Creates a new data object from a map. The map should contain values for all the properties of the data object. If
     * a value is missing, the default value of {@code 0}  (for numbers) or {@code false} (for booleans) is used.
     */
    public MomentumPIDControllerData(Map<String, Object> map) {
        this((double)map.getOrDefault("p", 0.0),
            (double)map.getOrDefault("i",0.0),
            (double)map.getOrDefault("d", 0.0),
            (double)map.getOrDefault("f", 0.0),
            (double)map.getOrDefault("errZone", 0.0),
            (double)map.getOrDefault("targetZone", 0.0),
            (double)map.getOrDefault("targetTime", 0.0),
            (double)map.getOrDefault("setpoint", 0.0),
            (boolean)map.getOrDefault("enabled", false));
    }

    public double getP() {
        return p;
    }

    public double getI() {
        return i;
    }

    public double getD() {
        return d;
    }

    public double getF() {
        return f;
    }

    public double getErrZone() {
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

    public MomentumPIDControllerData withP(double p) {
        return new MomentumPIDControllerData(p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    public MomentumPIDControllerData withI(double i) {
        return new MomentumPIDControllerData(p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    public MomentumPIDControllerData withD(double d) {
        return new MomentumPIDControllerData(p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    public MomentumPIDControllerData withF(double f) {
        return new MomentumPIDControllerData(p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    public MomentumPIDControllerData withErrZone(double errZone) {
        return new MomentumPIDControllerData(p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    public MomentumPIDControllerData withTargetZone(double targetZone) {
        return new MomentumPIDControllerData(p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    public MomentumPIDControllerData withTargetTime(double targetTime) {
        return new MomentumPIDControllerData(p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    public MomentumPIDControllerData withSetpoint(double setpoint) {
        return new MomentumPIDControllerData(p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    public MomentumPIDControllerData withEnabled(boolean enabled) {
        return new MomentumPIDControllerData(p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    @Override
    public Map<String, Object> asMap() {
        return ImmutableMap.<String, Object>builder()
        .put("p", p)
        .put("i", i)
        .put("d", d)
        .put("f", f)
        .put("errZone", errZone)
        .put("targetZone", targetZone)
        .put("targetTime", targetTime)
        .put("setpoint", setpoint)
        .put("enabled", enabled)
        .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MomentumPIDControllerData that = (MomentumPIDControllerData) obj;
        return this.p == that.p
            && this.i == that.i
            && this.d == that.d
            && this.f == that.f
            && this.errZone == that.errZone
            && this.targetZone == that.targetZone
            && this.targetTime == that.targetTime
            && this.setpoint == that.setpoint
            && this.enabled == that.enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    @Override
    public String toString() {
        return String.format("MomentumPIDControllerData(p=%s, i=%s, d=%s, f=%s, errZone=%s, targetZone=%s, targetTime=%s, setpoint=%s, enabled=%s)",
        p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

    @Override
    public String toHumanReadableString() {
        return String.format("p=%.3f, i=%.3f, d=%.3f, f=%.3f, errZone=%.3f, targetZone=%.3f, targetTime=%.3f, setpoint=%.3f, enabled=%s",
        p, i, d, f, errZone, targetZone, targetTime, setpoint, enabled);
    }

}