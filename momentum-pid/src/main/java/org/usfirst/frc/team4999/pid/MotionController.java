package org.usfirst.frc.team4999.pid;
public interface MotionController {
    /**
     * Calculate the next value for the MotionController to output
     */
    public void calculate();

    /**
     * Get the last calculated value
     * @return The last calculated value
     */
    public double get();

    /**
     * Get if the controller should be calculated
     * @return If the controller should have its calculate() method called
     */
    public boolean isEnabled();

    /**
     * Disable the output of the controller
     */
    public void zeroOutput();
}