package org.usfirst.frc.team4999.pid;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDOutput;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MomentumPIDTest {

    private PIDSource mockedSource;
    private PIDOutput mockedOutput;

    private double kP = 5, kI = 1.6, kD = 8.3, kF = 6.0, errZone = 10, targetZone = 1, targetTime = 3;

    private final double DELTA = 0.00001;
    
    public MomentumPIDTest() {
        mockedSource = mock(PIDSource.class);
        mockedOutput = mock(PIDOutput.class);
    }

    @Test
    public void testP() {
        double kP = this.kP;
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertEquals(kP, controller.getP(), DELTA);
        kP = 0.002;
        controller.setP(kP);
        assertEquals(kP, controller.getP(), DELTA);
    }

    @Test
    public void testI() {
        double kI = this.kI;
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertEquals(kI, controller.getI(), DELTA);
        kI = 0.05;
        controller.setI(kI);
        assertEquals(kI, controller.getI(), DELTA);
    }

    @Test
    public void testD() {
        double kD = this.kD;
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertEquals(kD, controller.getD(), DELTA);
        kD = 0.035;
        controller.setD(kD);
        assertEquals(kD, controller.getD(), DELTA);
    }
    
    @Test
    public void testF() {
        double kF = this.kF;
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertEquals(kF, controller.getF(), DELTA);
        kF = 0.012;
        controller.setF(kF);
        assertEquals(kF, controller.getF(), DELTA);
    }

    @Test
    public void testErrZone() {
        double errZone = this.errZone;
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertEquals(errZone, controller.getErrZone(), DELTA);
        errZone = 0.402;
        controller.setErrZone(errZone);
        assertEquals(errZone, controller.getErrZone(), DELTA);
    }

    @Test
    public void testTargetZone() {
        double targetZone = this.targetZone;
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertEquals(targetZone, controller.getTargetZone(), DELTA);
        targetZone = 0.12;
        controller.setTargetZone(targetZone);
        assertEquals(targetZone, controller.getTargetZone(), DELTA);
    }

    @Test
    public void testTargetTime() {
        double targetTime = this.targetTime;
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertEquals(targetTime, controller.getTargetTime(), DELTA);
        errZone = 0.0052;
        controller.setTargetTime(targetTime);
        assertEquals(targetTime, controller.getTargetTime(), DELTA);
    }

    @Test
    public void testSetpoint() {
        double setpoint = 0;
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertEquals(setpoint, controller.getSetpoint(), DELTA);
        setpoint = 10;
        controller.setSetpoint(setpoint);
        assertEquals(setpoint, controller.getSetpoint(), DELTA);
    }

    @Test
    public void testEnabled() {
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertFalse(controller.isEnabled());
        controller.enable();
        assertTrue(controller.isEnabled());
        controller.disable();
        assertFalse(controller.isEnabled());
        controller.setEnabled(true);
        assertTrue(controller.isEnabled());
        controller.setEnabled(false);
        assertFalse(controller.isEnabled());
    }

    @Test
    public void testGetCurrent() {
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        double get = 29.3;
        when(mockedSource.pidGet()).thenReturn(get);
        assertEquals(get, controller.getCurrentInput(), DELTA);
        get = -85.4;
        when(mockedSource.pidGet()).thenReturn(get);
        assertEquals(get, controller.getCurrentInput(), DELTA);
    }

    @Test
    public void testOnTarget() {
        double targetZone = 1;
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        controller.setSetpoint(0);
        when(mockedSource.pidGet()).thenReturn(5.0);
        assertFalse(controller.onTarget());
        
        controller.setSetpoint(19);
        when(mockedSource.pidGet()).thenReturn(18.001);
        assertTrue(controller.onTarget());

        when(mockedSource.pidGet()).thenReturn(19.999);
        assertTrue(controller.onTarget());
    }

    @Test
    public void testOnTargetForTime() {
        double targetZone = 1;
        double targetTime = 3;
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        controller.setSetpoint(10);
        when(mockedSource.pidGet()).thenReturn(5.0);
        assertFalse(controller.onTargetForTime());
        try {
            Thread.sleep((long) targetTime * 1000 + 50);
        } catch (InterruptedException e) {
            fail();
        }
        assertFalse(controller.onTargetForTime());
        when(mockedSource.pidGet()).thenReturn(10.9);
        assertFalse(controller.onTargetForTime());
        try {
            Thread.sleep((long) targetTime * 1000 + 50);
        } catch (InterruptedException e) {
            fail();
        }
        assertTrue(controller.onTargetForTime());
    }

    boolean updated = false;
    @Test
    public void testUpdate() {
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        controller.setListener(()->{updated = true;});
        controller.setP(5);
        assertTrue(updated);
        updated = false;
        controller.setI(8);
        assertTrue(updated);
        updated = false;
        controller.setD(9);
        assertTrue(updated);
        updated = false;
        controller.setF(6);
        assertTrue(updated);
        updated = false;
        controller.setErrZone(10);
        assertTrue(updated);
        updated = false;
        controller.setTargetZone(5);
        assertTrue(updated);
        updated = false;
        controller.setTargetTime(5);
        assertTrue(updated);
        updated = false;
    }

    @Test
    public void testName() {
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertEquals("Test Controller", controller.getName());
        controller.setName("Some Vital PID Controller");
        assertEquals("Some Vital PID Controller", controller.getName());
    }

    @Test 
    public void testSubsystem() {
        MomentumPID controller = new MomentumPID("Test Controller", kP, kI, kD, kF, errZone, targetZone, targetTime, mockedSource, mockedOutput);
        assertEquals("Ungrouped", controller.getSubsystem());
        controller.setSubsystem("This Vital Subsystem");
        assertEquals("This Vital Subsystem", controller.getSubsystem());
    }

}