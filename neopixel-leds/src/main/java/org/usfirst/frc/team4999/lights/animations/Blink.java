package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

public class Blink implements Animation {
	
	private Color[] colors;
	private int[] waittimes;
	
	private int idx;
	
	/**
	 * Switches between colors
	 * @param colors colors to switch between
	 * @param waittime how long to wait before switching color
	 */
	public Blink(Color[] colors, int waittime) {
		int[] waittimes = new int[colors.length];
		for(int i = 0; i < waittimes.length; i++) {
			waittimes[i] = waittime;
		}
		this.colors = colors;
		this.waittimes = waittimes;
		idx = 0;
	}
	
	/**
	 * Switches between colors
	 * @param colors colors to switch between
	 * @param waittimes how long to wait for each color before switching
	 */
	public Blink(Color[] colors, int[] waittimes) {
		if(colors.length != waittimes.length) throw new IllegalArgumentException("Need exactly one waittime for every color");
		this.colors = colors;
		this.waittimes = waittimes;
		idx = 0;
	}

	@Override
	public Packet[] getNextFrame() {
		Packet out[] =  {Commands.makeStride(0, colors[idx], 1, 1)};
		idx = (idx + 1) % waittimes.length;
		return out;
	}

	@Override
	public int getFrameDelayMilliseconds() {
		return waittimes[idx];
	}

}
