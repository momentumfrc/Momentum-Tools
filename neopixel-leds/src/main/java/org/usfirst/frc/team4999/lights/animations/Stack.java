package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

public class Stack implements Animation {
	
	Color[] colors;
	int delay;
	
	Color[] buffer, lastbuffer;
	int coloridx;
	int width, idx;
	
	Color background = Color.WHITE;
	

	public Stack(Color[] colors, int size, int delay) {
		this.colors = colors;
		this.delay = delay;
		
		buffer = new Color[size];
		for(int i = 0; i < size; i++) {
			buffer[i] = background;
		}
		
		lastbuffer = buffer.clone();
		
		width = 0;
		idx = buffer.length;
	}

	@Override
	public Packet[] getNextFrame() {
		if(idx < buffer.length) {
			buffer[idx] = background;
		}
		idx--;
		buffer[idx] = colors[coloridx];
		if(idx == width) {
			for(int i = 0; i < width; i++ ) {
				buffer[i] = colors[coloridx];
			}
			for(int i = width+1; i < buffer.length; i++) {
				buffer[i] = background;
			}
			idx = buffer.length;
			coloridx = (coloridx + 1) % colors.length;
			width++;
			if(width == buffer.length) {
				width = 0;
				for(int i = 0; i < buffer.length; i++) {
					buffer[i] = background;
				}
			}
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
