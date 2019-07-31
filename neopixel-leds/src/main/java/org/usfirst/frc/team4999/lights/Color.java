package org.usfirst.frc.team4999.lights;


/**
 * Holds a basic RGB color
 * @author jordan
 *
 */
public class Color {
	private final int r,g,b;
	
	/**
	 * Makes a new color with the specified values between 0-255
	 * @param r Red channel
	 * @param g Green channel
	 * @param b Blue channel
	 */
	public Color(int r, int g, int b) {
		if(!(checkBounds(r) && checkBounds(g) && checkBounds(b))) {
			System.out.format("Invalid color: R:%d, G:%d, B:%d\n", r, g, b);
			throw new IllegalArgumentException("All values must be greater than zero and less than 255");
		}
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	private boolean checkBounds(int i) {
		return i >= 0 && i < 256;
	}
	
	/**
	 * Get the red value of the color
	 * @return red channel value
	 */
	public int getRed() {
		return r;
	}
	/**
	 * Get the green value of the color
	 * @return green channel value
	 */
	public int getGreen() {
		return g;
	}
	/**
	 * Get the blue value of the color
	 * @return blue channel value
	 */
	public int getBlue() {
		return b;
	}
	
	public String toString() {
		return String.format("R:%d G:%d B:%d", r,g,b);
	}

	public boolean equals(Object o) {
		if(o instanceof Color) {
			Color other = (Color) o;
			return getRed() == other.getRed()
					&& getGreen() == other.getGreen()
					&& getBlue() == other.getBlue();
		} else  {
			return false;
		}
	}

	public static final Color RED = new Color(255,0,0);
	public static final Color YELLOW = new Color(255,255,0);
	public static final Color GREEN = new Color(0,255,0);
	public static final Color BLUE = new Color(0,0,255);
	public static final Color BLACK = new Color(0,0,0);
	public static final Color WHITE = new Color(255,255,255);
	public static final Color MOMENTUM_BLUE = new Color(6,206,255);
	public static final Color MOMENTUM_PURPLE = new Color(159,1,255);
	
	
}
