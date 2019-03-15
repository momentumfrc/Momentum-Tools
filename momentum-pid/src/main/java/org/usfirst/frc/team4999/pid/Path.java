package org.usfirst.frc.team4999.pid;
public class Path {
    private double[][] path;
    private double dt;

    public Path(double[][] path, double dt) {
        this.path = path;
        this.dt = dt;
    }

    public double[] get(double t) {
        int idx = (int)(t / dt);
        return path[idx];
    }

    public double getMaxT() {
        return (path.length-1) * dt;
    }

    public double[] getAtT(double time) {
        if(time > getMaxT()) {
            return get(path.length - 1);
        } else {
            return get((int)(time / dt));
        }
    }

}