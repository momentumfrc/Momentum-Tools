package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

public class RandomColors implements Animation {
	
	private int delay, repeat;
	
	/**
	 * Creates a pattern of random colors and repeates it every repeat steps
	 * @param delay how long to wait before creating a new random pattern. -1 indicates only set one pattern
	 * @param repeat how long to make the pattern of random colors
	 */
	public RandomColors(int delay, int repeat) {
		this.delay = delay;
		this.repeat = repeat;
	}
	
	
	private int randomRGB() {
		return (int)(Math.random() * 256);
	}
	
	@Override
	public Packet[] getNextFrame() {
		Packet[] out = new Packet[repeat];
		for(int i = 0; i < out.length; i++) {
			Color paint = new Color(randomRGB(), randomRGB(), randomRGB());
			out[i] = Commands.makeStride(i, paint, 1, repeat );
		}
		return out;
	}

	@Override
	public int getFrameDelayMilliseconds() {
		return delay;
	}

}
