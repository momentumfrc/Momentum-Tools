package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

public class TwoColors implements Animation {
	private static final int DELAY = 100;
	Color color1;
	Color color2;
	int length, skip;
	
	public TwoColors(Color color1, Color color2, int length) {
		this.color1 = color1;
		this.color2 = color2;
		this.length = length;
		skip = length/2;
		
	}

	@Override
	public Packet[] getNextFrame() {
		Packet[] out = new Packet[2];
		out[0] = Commands.makeStride(0, color1, skip, skip);
		out[1] = Commands.makeStride(skip, color2, skip, skip);
		return out;
	}

	@Override
	public int getFrameDelayMilliseconds() {
		return DELAY;
	}

}
