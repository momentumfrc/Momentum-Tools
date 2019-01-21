package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

public class Bounce implements Animation {
	
	private Color background;
	private Color[] run;
	private int[] pos, v;
	private int area;
	
	private int delay;
	
	private Color blendInto(Color bg, Color fg, double percentbg) {
		//System.out.println("Blending [" + bg.toString() +"] into ["+fg.toString()+"] with percent " + percentbg);
		int r = fg.getRed() + (int)((bg.getRed() - fg.getRed()) * percentbg);
		int g = fg.getGreen() + (int)((bg.getGreen() - fg.getGreen()) * percentbg);
		int b = fg.getBlue() + (int)((bg.getBlue() - fg.getBlue()) * percentbg);
		return new Color(r,g,b);
	}
	

	public Bounce(Color background, Color foreground, int fadelength, int bounceArea, int delay) {
		this.background = background;
		run = new Color[fadelength + 1];
		pos = new int[run.length];
		v = new int[pos.length];
		for(int i = 0; i < run.length; i++) {
			run[run.length-1-i] = blendInto(background, foreground, (i+1)/(double)(run.length));
		}
		for(int i = run.length - 1; i >= 0; i--) {
			pos[i] = i;
		}
		for(int i = 0; i < v.length; i++) {
			v[i] = 1;
		}
		area = bounceArea;
		this.delay = delay;
	}
	
	public Bounce(Color background, Color[] bouncer, int bounceArea, int delay) {
		this.background = background;
		run = bouncer;
		pos = new int[run.length];
		v = new int[run.length];
		for(int i = run.length - 1; i >= 0; i--) {
			pos[i] = i;
		}
		for(int i = 0; i < v.length; i++) {
			v[i] = 1;
		}
		area = bounceArea;
		this.delay = delay;
		
	}

	@Override
	public Packet[] getNextFrame() {
		Packet[] out = new Packet[1+pos.length];
		out[0] = Commands.makeStride(0, background, 1, 1);
		for(int i = 0; i < pos.length; i++) {
			out[i+1] = Commands.makeStride(pos[i], run[i], 1, area);
		}
		for(int i = 0; i < v.length; i++) {
			if(pos[i] >= area)
				v[i] = -Math.abs(v[i]);
			else if(pos[i] <= 0)
				v[i] = Math.abs(v[i]);
			pos[i] = pos[i] + v[i];
		}
		return out;
	}

	@Override
	public int getFrameDelayMilliseconds() {
		return delay;
	}

}
