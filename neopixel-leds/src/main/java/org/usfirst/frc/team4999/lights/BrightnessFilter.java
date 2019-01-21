package org.usfirst.frc.team4999.lights;

import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTableEntry;


public class BrightnessFilter {
	
	private static double brightness = 0.4;
	
	private static BrightnessFilter instance;
	
	private final String key = "BRIGHTNESS";
	
	private final NetworkTableEntry entry;
	
	private BrightnessFilter() {
		
		entry = SmartDashboard.getEntry(key);
		entry.setNumber(brightness);
		
		entry.addListener((notification) -> {
			brightness = truncate(notification.value.getDouble());
		}, TableEntryListener.kUpdate|TableEntryListener.kImmediate);
		
	}
	
	public static void setBrightness(double brightness) {
		BrightnessFilter.brightness = truncate(brightness);
	}
	

	
	private static double truncate(double in) {
		if(in > 1) return 1;
		if(in < 0) return 0;
		return in;
	}
	
	/**
	 * Registers the brightness class to listen to changes in the robot preferences
	 */
	public static void register() {
		if(instance == null)
			instance = new BrightnessFilter();
	}
	
	public static int dimValue(int value) {
		return (int)(value * brightness);
	}
	
	public static Color dimColor(Color in) {
		return new Color(dimValue(in.getRed()), dimValue(in.getGreen()), dimValue(in.getBlue()));
	}

}
