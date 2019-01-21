package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

public class Solid implements Animation {
	
	private static int DELAY = 500;
	
	Color[] color;
	
	/**
	 * Loops through the colors of the rainbow
	 * @return the solid animation
	 */
	public static Solid rainbow() {
		Color[] rainbow = {
				Color.RED,
				new Color(255,127,0),
				Color.YELLOW,
				Color.GREEN,
				Color.BLUE,
				new Color(139,0,255)
		};
		return new Solid(rainbow);
	}
	
	/**
	 * Fills with a pattern of colors
	 * @param colors the pattern to paint
	 */
	public Solid(Color[] colors) {
		this.color = colors;
	}
	
	/**
	 * Fills with a single color
	 * @param color the color to fill
	 */
	public Solid(Color color) {
		this.color = new Color[]{color};
	}

	@Override
	public Packet[] getNextFrame() {
		Packet[] out = new Packet[color.length];
		
		for(int i = 0; i < out.length; i++) {
			out[i] = Commands.makeStride(i, color[i % color.length], 1, color.length);
		}
		return out;
	}

	@Override
	public int getFrameDelayMilliseconds() {
		return DELAY;
	}

}
