package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

class DifferenceCalculator {
	double[] color = new double[3];
	double[] diffs = new double[3];
	double[] to = new double[3];
	
	private final double CLOSE_ENOUGH = 0.005;
	
	public DifferenceCalculator(Color from) {
		color[0] = from.getRed();
		color[1] = from.getGreen();
		color[2] = from.getBlue();
	}
	
	public void calculateDiffs(Color to, int steps) {
		this.to[0] = to.getRed();
		this.to[1] = to.getGreen();
		this.to[2] = to.getBlue();
		diffs[0] = (this.to[0] - color[0]) / steps;
		diffs[1] = (this.to[1] - color[1]) / steps;
		diffs[2] = (this.to[2] - color[2]) / steps;
	}
	
	public void applyDiffs() {
		color[0] += diffs[0];
		color[1] += diffs[1];
		color[2] += diffs[2];		
	}
	
	public boolean atTarget() {
		return ((Math.abs(color[0] - to[0]) < CLOSE_ENOUGH) && (Math.abs(color[1] - to[1]) < CLOSE_ENOUGH) && (Math.abs(color[2] - to[2]) < CLOSE_ENOUGH));
	}
	
	public Color toColor() {
		int[] out = new int[3];
		
		// downcast the doubles into ints
		out[0] = (int) color[0];
		out[1] = (int) color[1];
		out[2] = (int) color[2];
		
		// limit the ints to within [0,255]
		out[0] = (out[0] > 255) ? 255 : out[0];
		out[1] = (out[1] > 255) ? 255 : out[1];
		out[2] = (out[2] > 255) ? 255 : out[2];
		
		out[0] = (out[0] < 0) ? 0 : out[0];
		out[1] = (out[1] < 0) ? 0 : out[1];
		out[2] = (out[2] < 0) ? 0 : out[2];
		
		return new Color(out[0], out[1], out[2]);
	}
	
	
}

public class Fade implements Animation {
	private Color[] colors;
	private DifferenceCalculator current;
	private int fadeTime, holdTime;
	
	private int idx = 0;
	private boolean hold = false;
	
	private final int STEPS = 50;
	
	/**
	 * Fade between the 6 colors of the rainbow
	 * @param fadeTime time spent fading to next color
	 * @param holdTime duration to hold at a color
	 * @return the Fade animation
	 */
	public static Fade rainbowFade(int fadeTime, int holdTime) {
		return new Fade(new Color[]{
				Color.RED,
				new Color(255,127,0),
				Color.YELLOW,
				Color.GREEN,
				Color.BLUE,
				new Color(139,0,255)
		}, fadeTime, holdTime);
	}
	
	/**
	 * Fade between colors
	 * @param colors colors to fade between
	 * @param fadeTime time spent fading to next color
	 * @param holdTime duration to hold at a color
	 */
	public Fade(Color[] colors, int fadeTime, int holdTime) {
		this.colors = colors;
		current = new DifferenceCalculator(colors[0]);
		current.calculateDiffs(colors[getNextIndex()], STEPS);
		this.fadeTime = fadeTime;
		this.holdTime = holdTime;
		
	}
	
	@Override
	public Packet[] getNextFrame() {
		current.applyDiffs();
		if(current.atTarget()) {
			idx = getNextIndex();
			hold = true;
			current.calculateDiffs(colors[idx], STEPS);
		}
		return new Packet[] { Commands.makeStride(0, current.toColor(), 1, 1) };
	}

	@Override
	public int getFrameDelayMilliseconds() {
		if(hold) {
			hold = false;
			return holdTime;
		} else {
			return fadeTime/STEPS;
		}
	}
	
	private int getNextIndex() {
		return (idx + 1 ) % colors.length;
	}
	
}
