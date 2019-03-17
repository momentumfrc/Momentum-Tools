package org.usfirst.frc.team4999.pid;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;

import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SendableCANPIDControllerTest {
    CANSparkMax mockedSpark;
    CANPIDController mockedPIDController;
    CANEncoder mockedEncoder;
    
    private double mocked_p, mocked_i, mocked_d, mocked_f, mocked_errZone;

    private double targetZone = 23.0, targetTime = 5.0;

    private static final double DELTA = 0.00001;

    public SendableCANPIDControllerTest() {
        mockedPIDController = mock(CANPIDController.class);

        Answer<Object> answer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                switch(invocation.getMethod().getName()) {
                    case "getP":
                        return mocked_p;
                    case "setP":
                        mocked_p = invocation.getArgument(0);
                        return null;
                    case "getI":
                        return mocked_i;
                    case "setI":
                        mocked_i = invocation.getArgument(0);
                        return null;
                    case "getD":
                        return mocked_d;
                    case "setD":
                        mocked_d = invocation.getArgument(0);
                        return null;
                    case "getFF":
                        return mocked_f;
                    case "setFF":
                        mocked_f = invocation.getArgument(0);
                        return null;
                    case "getIZone":
                        return mocked_errZone;
                    case "setIZone":
                        mocked_errZone = invocation.getArgument(0);
                        return null;
                }
                return new Object();
            }
        };

        when(mockedPIDController.getP()).then(answer);
        when(mockedPIDController.setP(anyDouble())).then(answer);
        when(mockedPIDController.getI()).then(answer);
        when(mockedPIDController.setI(anyDouble())).then(answer);
        when(mockedPIDController.getD()).then(answer);
        when(mockedPIDController.setD(anyDouble())).then(answer);
        when(mockedPIDController.getFF()).then(answer);
        when(mockedPIDController.setFF(anyDouble())).then(answer);
        when(mockedPIDController.getIZone()).then(answer);
        when(mockedPIDController.setIZone(anyDouble())).then(answer);

        mockedEncoder = mock(CANEncoder.class);
        
        mockedSpark = mock(CANSparkMax.class);
        when(mockedSpark.getPIDController()).thenReturn(mockedPIDController);
        when(mockedSpark.getEncoder()).thenReturn(mockedEncoder);
    }

    @Test
    public void testP() {
        double p = this.mocked_p;
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals(p, controller.getP(), DELTA);
        p = 8.6;
        controller.setP(p);
        assertEquals(p, this.mocked_p, DELTA);
        assertEquals(p, controller.getP(), DELTA);
        controller.disable();
        assertEquals(p, this.mocked_p, DELTA);
        assertEquals(p, controller.getP(), DELTA);
        p = 10;
        controller.setP(p);
        assertEquals(p, this.mocked_p, DELTA);
        assertEquals(p, controller.getP(), DELTA);
        controller.enable();
        assertEquals(p, this.mocked_p, DELTA);
        assertEquals(p, controller.getP(), DELTA);
    }

    @Test
    public void testI() {
        double i = this.mocked_i;
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals(i, controller.getI(), DELTA);
        i = 8.6;
        controller.setI(i);
        assertEquals(i, this.mocked_i, DELTA);
        assertEquals(i, controller.getI(), DELTA);
        controller.disable();
        assertEquals(i, this.mocked_i, DELTA);
        assertEquals(i, controller.getI(), DELTA);
        i = 10;
        controller.setI(i);
        assertEquals(i, this.mocked_i, DELTA);
        assertEquals(i, controller.getI(), DELTA);
        controller.enable();
        assertEquals(i, this.mocked_i, DELTA);
        assertEquals(i, controller.getI(), DELTA);
    }

    @Test
    public void testD() {
        double d = this.mocked_d;
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals(d, controller.getD(), DELTA);
        d = 8.6;
        controller.setD(d);
        assertEquals(d, this.mocked_d, DELTA);
        assertEquals(d, controller.getD(), DELTA);
        controller.disable();
        assertEquals(d, this.mocked_d, DELTA);
        assertEquals(d, controller.getD(), DELTA);
        d = 10;
        controller.setD(d);
        assertEquals(d, this.mocked_d, DELTA);
        assertEquals(d, controller.getD(), DELTA);
        controller.enable();
        assertEquals(d, this.mocked_d, DELTA);
        assertEquals(d, controller.getD(), DELTA);
    }

    @Test
    public void testF() {
        double f = this.mocked_f;
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals(f, controller.getF(), DELTA);
        f = 8.6;
        controller.setF(f);
        assertEquals(f, this.mocked_f, DELTA);
        assertEquals(f, controller.getF(), DELTA);
        controller.disable();
        assertEquals(f, this.mocked_f, DELTA);
        assertEquals(f, controller.getF(), DELTA);
        f = 10;
        controller.setF(f);
        assertEquals(f, this.mocked_f, DELTA);
        assertEquals(f, controller.getF(), DELTA);
        controller.enable();
        assertEquals(f, this.mocked_f, DELTA);
        assertEquals(f, controller.getF(), DELTA);
    }

    @Test
    public void testErrZone() {
        double errZone = this.mocked_errZone;
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals(errZone, controller.getErrZone(), DELTA);
        errZone = 8.6;
        controller.setErrZone(errZone);
        assertEquals(errZone, this.mocked_errZone, DELTA);
        assertEquals(errZone, controller.getErrZone(), DELTA);
        controller.disable();
        assertEquals(errZone, this.mocked_errZone, DELTA);
        assertEquals(errZone, controller.getErrZone(), DELTA);
        errZone = 10;
        controller.setErrZone(errZone);
        assertEquals(errZone, this.mocked_errZone, DELTA);
        assertEquals(errZone, controller.getErrZone(), DELTA);
        controller.enable();
        assertEquals(errZone, this.mocked_errZone, DELTA);
        assertEquals(errZone, controller.getErrZone(), DELTA);
    }

    @Test
    public void testTargetZone() {
        double targetZone = this.targetZone;
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals(targetZone, controller.getTargetZone(), DELTA);
        targetZone = 56.45;
        controller.setTargetZone(targetZone);
        assertEquals(targetZone, controller.getTargetZone(), DELTA);
    }

    @Test
    public void testTargetTime() {
        double targetTime = this.targetTime;
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals(targetTime, controller.getTargetTime(), DELTA);
        targetTime = 40.6;
        controller.setTargetTime(targetTime);
        assertEquals(targetTime, controller.getTargetTime(), DELTA);
    }

    @Test
    public void testSetpoint() {
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals(0, controller.getSetpoint(), DELTA);
        double setpoint = 10;
        controller.setSetpoint(setpoint);
        assertEquals(setpoint, controller.getSetpoint(), DELTA);
    }

    @Test
    public void testEnabled() {
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertFalse(controller.isEnabled());
        controller.disable();
        assertFalse(controller.isEnabled());
        controller.enable();
        assertTrue(controller.isEnabled());
        controller.setEnabled(false);
        assertFalse(controller.isEnabled());
        controller.setEnabled(true);
        assertTrue(controller.isEnabled());
    }

    @Test
    public void testControlType() {
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals(controller.getControlType(), ControlType.kPosition);
        controller.setControlType(ControlType.kVelocity);
        assertEquals(controller.getControlType(), ControlType.kVelocity);
    }

    @Test
    public void testOnTarget() {
        double targetZone = 1;
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        controller.setSetpoint(10);
        when(mockedEncoder.getPosition()).thenReturn(5.0);
        assertFalse(controller.onTarget());
        when(mockedEncoder.getPosition()).thenReturn(9.1);
        assertTrue(controller.onTarget());
        when(mockedEncoder.getPosition()).thenReturn(10.999);
        assertTrue(controller.onTarget());
        controller.setSetpoint(5);
        assertFalse(controller.onTarget());
    }

    @Test
    public void testOnTargetForTime() {
        double targetZone = 1;
        double targetTime = 3;
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        controller.setSetpoint(10);
        when(mockedEncoder.getPosition()).thenReturn(5.0);
        assertFalse(controller.onTargetForTime());
        try {
            Thread.sleep((long) targetTime * 1000 + 50);
        } catch (InterruptedException e) {
            fail();
        }
        assertFalse(controller.onTargetForTime());
        when(mockedEncoder.getPosition()).thenReturn(10.9);
        assertFalse(controller.onTargetForTime());
        try {
            Thread.sleep((long) targetTime * 1000 + 50);
        } catch (InterruptedException e) {
            fail();
        }
        assertTrue(controller.onTargetForTime());
    }

    boolean burned = false;
    @Test
    public void testBurnFlash() {
        when(mockedSpark.burnFlash()).then(new Answer<CANError>() { public CANError answer(InvocationOnMock i) { burned = true; return null;}});
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);

        controller.saveValues();
        assertFalse(burned);

        controller.setP(10.0);
        controller.setI(56);
        controller.setD(4);
        controller.setF(24);
        controller.setErrZone(6);
        controller.saveValues();
        assertTrue(burned);
        burned = false;

        controller.saveValues();
        assertFalse(burned);

        controller.disable();
        assertFalse(burned);
        controller.enable();
        assertFalse(burned);

        controller.setP(65.3);
        controller.disable();
        assertFalse(burned);

        controller.enable();
        assertTrue(burned);

    }

    boolean updated = false;
    @Test
    public void testUpdate() {
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
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
    }

    @Test
    public void testName() {
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals("Test Controller", controller.getName());
        controller.setName("Some Vital PID Controller");
        assertEquals("Some Vital PID Controller", controller.getName());
    }

    @Test 
    public void testSubsystem() {
        SendableCANPIDController controller = new SendableCANPIDController("Test Controller", mockedSpark, targetZone, targetTime);
        assertEquals("Ungrouped", controller.getSubsystem());
        controller.setSubsystem("This Vital Subsystem");
        assertEquals("This Vital Subsystem", controller.getSubsystem());
    }

}