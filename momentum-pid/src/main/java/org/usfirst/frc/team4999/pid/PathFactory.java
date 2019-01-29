package org.usfirst.frc.team4999.pid;
public class PathFactory {
    
    /**
     * Calculate a Path for a given displacement, max velocity, max acceleration, at a specified time interval
     * Units of parameters should all agree (dt is in miliseconds, i displacement is meters, then max_v should be m/ms and max_a should be m/ms/ms)
     * @param displacement The displacement path to calculate
     * @param dt How frequently nodes on the path should be calculated, in miliseconds
     * @param max_v The maximum velocity the path should allow
     * @param max_a The maximum acceleration the path should allow
     * @return A path object will move the specified displacement as fast as possible within the maximum velocity and acceleration restrictions
     */
    public static Path path_1D(double displacement, double dt, double initial_v, double max_v, double max_a) {
        /*
        Standard equation: vf^2 = v0^2 + 2 * a * dx
        Max velocity should be achieved halfway through path, so at dx = displacement/2
        vf^2 = v0^2 + a * displacement
        vf = sqrt(v0 * v0 + a * displacement)
        */
        double theoretical_max_v = Math.sqrt(initial_v * initial_v + max_a * displacement);
        double cruise_v = Math.min(theoretical_max_v, max_v);

        /*
        Standard equation: vf^2 = v0^2 + 2 * a * dx
        vf is cruise velocity
        2 * a * dx = cruise_v^2 - v0^2
        dx = (cruise_v * cruise_v - v0 * v0) / ( 2 * a )
        */
        double accel_dx = (cruise_v * cruise_v - initial_v * initial_v) / ( 2 * max_a);

        /*
        Displacement to accelerate + displacement to cruise + displacement to decelerate should equal total displacement
        displacement = accel_dx + cruise_dx + decel_dx
        Acceleration and deceleration displacements are equal since the magnitude of the accelerations are equal, just opposite signs
        decel_dx = accel_dx
        displacement = 2 * accel_dx + cruise dx
        cruise_dx = displacement - 2 * accel_dx
        */
        double cruise_dx = displacement - 2*accel_dx;

        /*
        Standard equation: dx = v0 * t + (1/2) * a * t^2
        (1/2) * a * t^2 + v0 * t - dx = 0
        Solve quadratic for t
        t = ( -v0 +- sqrt(v0^2 + 2*a*dx) ) / a

        Since sqrt(v0^2 + 2*a*dx) > v0 for any a > 0,
        then -v0 + sqrt(v0^2 + 2*a*dx) > 0 but -v0 - sqrt(v0^2 + 2*a*dx) < 0
        Since we want t > 0, we want to use -v0 + sqrt(v0^2 + 2*a*dx)

        So t = ( sqrt(v0^2 + 2*a*dx) - v0 ) / a
        */
        double accel_time = (Math.sqrt(initial_v * initial_v + 2 * max_a * accel_dx) - initial_v) / max_a;

        /*
        Standard equation: dx = v0 * t + (1/2) * a * t^2
        Cruising at constant velocity, v0 = cruise_velocity, so a = 0
        dx = cruise_velocity * t
        t = dx / cruise_velocity
        */
        double cruise_time = cruise_dx / cruise_v;

        double[][] path = new double[(int)((accel_time + cruise_time + accel_time)/dt)][3]; // [displacement, velocity, acceleration]
        for(int i = 0; i < path.length; i++) {
            double time = i * dt;
            double x,v,a;
            if (time < accel_time) {
                // Accelerating
                // xf = x0 + v0 * t + (1/2) * a * t^2
                x = 0 + (initial_v * time) + (max_a * time * time) / 2;
                // vf = v0 + a * t
                v = initial_v + max_a * time;
                a = max_a;
            } else if (time > accel_time + cruise_time) {
                // Decelerating
                // time in equations is relative to the start of the deceleration
                time = time - (accel_time + cruise_time);
                // xf = x0 + v0 * t + (1/2) * a * t^2
                x = (accel_dx + cruise_dx) + (cruise_v * time) + (-max_a * time * time) / 2;
                // vf = v0 + a * t
                v = cruise_v + -max_a * time;
                a = -max_a;
            } else {
                // Cruising
                // time in equations is relative to the start of the deceleration
                time = time - accel_time;
                // xf = x0 + v0 * t + (1/2) * a * t^2
                // but a = 0, so xf = x0 + v0 * t
                x = accel_dx + cruise_time * time;
                // vf = v0 + a * t
                // but a = 0, so vf = v0
                v = cruise_v;
                a = 0;
            }
            path[i] = new double[]{x,v,a};
        }
        return new Path(path, dt);
    }

}