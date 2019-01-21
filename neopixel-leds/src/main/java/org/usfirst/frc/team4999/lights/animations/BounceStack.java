package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

public class BounceStack implements Animation {
	
	int delay;
	
	Color[] buffer;
	Color currentcolor;
	
	int stopidx, startidx, currentidx;
	
	Color background = Color.WHITE;

	public BounceStack(Color[] colors, int bgsize, int delay) {
		this.delay = delay;
		
		buffer = new Color[colors.length+bgsize];
		int i = 0;
		for(; i < colors.length; i++) {
			buffer[i] = colors[i];
		}
		for(; i < buffer.length; i++) {
			buffer[i] = background;
		}
		stopidx = colors.length;
		startidx = 0;
		currentidx = startidx;
		startidx = (startidx + 1) % buffer.length;
	}

	@Override
	public Packet[] getNextFrame() {
		
		currentcolor = buffer[currentidx];
		buffer[currentidx] = background;
		
		currentidx = (currentidx + buffer.length - 1) % buffer.length;
		
		buffer[currentidx] = currentcolor;
		
		if(currentidx == stopidx) {
			stopidx = (stopidx + 1) % buffer.length;
			currentidx = startidx;
			startidx = (startidx + 1) % buffer.length;
		}
		
		
		Packet[] out = new Packet[buffer.length];
		
		
		for(int i = 0; i < buffer.length; i++) {
			out[i] = Commands.makeStride(i, buffer[i], 1, buffer.length);
		}
		
		return out;
	}

	@Override
	public int getFrameDelayMilliseconds() {
		return delay;
	}

}
