package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

public class SolidPatch implements Animation {

    private static final int FRAME_DELAY = 500;

    private int startidx;
    private int stopidx;
    private Color color;

    public SolidPatch(Color color, int startidx, int stopidx) {
        this.color = color;
        this.startidx = startidx;
        this.stopidx = stopidx;
    }

    @Override
    public Packet[] getNextFrame() {
        return new Packet[]{Commands.makeRun(startidx, color, (stopidx - startidx) + 1)};
    }

    @Override
    public int getFrameDelayMilliseconds() {
        return FRAME_DELAY;
    }

}
