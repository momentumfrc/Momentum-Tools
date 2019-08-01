package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Packet;

import static org.usfirst.frc.team4999.lights.AnimationUtils.*;

public class Solid implements Animation {
	
	private static int DELAY = 500;
	
	Color[] colors;
	
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
		this.colors = colors;
	}
	
	/**
	 * Fills with a single color
	 * @param color the color to fill
	 */
	public Solid(Color color) {
		this.colors = new Color[]{color};
	}

	@Override
	public Packet[] getNextFrame() {
		return displayColorBuffer(colors);
	}

	@Override
	public int getFrameDelayMilliseconds() {
		return DELAY;
	}

}
