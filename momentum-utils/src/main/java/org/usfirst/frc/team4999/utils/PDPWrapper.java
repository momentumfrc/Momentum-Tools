package org.usfirst.frc.team4999.utils;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;

public class PDPWrapper {
	
	private PowerDistributionPanel pdp;
	
	private long[] timers = new long[16];
	
	private long voltagetimer;
	
	private long getTimeMillis() {
		return (long)Timer.getFPGATimestamp() * 1000;
	}

	public PDPWrapper(PowerDistributionPanel pdp) {
		this.pdp = pdp;
		for(int i = 0; i < timers.length; i++) {
			timers[i] = getTimeMillis();
		}
	}
	
	/**
	 * Checks if a channel of the PDP has been above the specified current for the specified period of time
	 * @param channel Channel of the PDP to check
	 * @param current Cutoff current in amps
	 * @param cutofftime Cutoff time in milliseconds
	 * @return If the channel is over the current limit
	 */
	public boolean checkOvercurrent(int channel, double current, int cutofftime) {
		if(pdp.getCurrent(channel) > current)
			return getTimeMillis() - timers[channel] > cutofftime;
		else
			timers[channel] = getTimeMillis();
		return false;
	}
	
	/**
	 * Checks if some channels of the PDP has been above the specified current for the specified period of time
	 * @param channels Channels of the PDP to check
	 * @param current Cutoff current in amps
	 * @param cutofftime Cutoff time in milliseconds
	 * @return If any of the channels are over the current limit
	 */
	public boolean checkOvercurrent(int[] channels, double current, int cutofftime) {
		for(int channel : channels) {
			if(checkOvercurrent(channel, current, cutofftime))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if a channel of the PDP has been below the specified current for the specified period of time
	 * @param channel Channel of the PDP to check
	 * @param current Cutoff current in amps
	 * @param cutofftime Cutoff time in milliseconds
	 * @return If the channel is under the current limit
	 */
	public boolean checkUndercurrent(int channel, double current, int cutofftime) {
		if(pdp.getCurrent(channel) < current)
			return getTimeMillis() - timers[channel] > cutofftime;
		else
			timers[channel] = getTimeMillis();
		return false;
	}
	
	/**
	 * Checks if some channels of the PDP has been below the specified current for the specified period of time
	 * @param channels Channels of the PDP to check
	 * @param current Cutoff current in amps
	 * @param cutofftime Cutoff time in milliseconds
	 * @return If any of the channels are below the current limit
	 */
	public boolean checkUndercurrent(int[] channels, double current, int cutofftime) {
		for(int channel : channels) {
			if(checkUndercurrent(channel, current, cutofftime))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if the battery voltage has been below the specified voltage for the specified period of time
	 * @param voltage Cutoff voltage in volts
	 * @param cutofftime Cutoff time in milliseconds
	 * @return If the voltage is below the voltage limit
	 */
	public boolean checkUndervoltage(double voltage, int cutofftime) {
		if(pdp.getVoltage() < voltage) {
			return getTimeMillis() - voltagetimer < cutofftime;
		} else {
			voltagetimer = getTimeMillis();
		}
		return false;
	}

}