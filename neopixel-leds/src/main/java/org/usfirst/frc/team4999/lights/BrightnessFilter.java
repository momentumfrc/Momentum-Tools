package org.usfirst.frc.team4999.lights;

public class BrightnessFilter {
	
	private static double brightness = 0.4;
	
	public static void setBrightness(double brightness) {
		BrightnessFilter.brightness = truncate(brightness);
	}
	

	
	private static double truncate(double in) {
		if(in > 1) return 1;
		if(in < 0) return 0;
		return in;
	}
	
	public static int dimValue(int value) {
		return (int)(value * brightness);
	}
	
	public static Color dimColor(Color in) {
		return new Color(dimValue(in.getRed()), dimValue(in.getGreen()), dimValue(in.getBlue()));
	}

}
