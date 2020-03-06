package org.usfirst.frc.team4999.utils;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;

public class MoPDP extends PowerDistributionPanel {

  /**
   * Checks if a channel of the PDP has been above the current threshold for the
   * specified period of time
   * 
   * @param channel          Channel(s) of the PDP to check
   * @param currentThreshold Max current allowed
   * @param timeThreshold    Cutoff time in milliseconds
   */
  public class OvercurrentMonitor {
    private final int[] channels;
    private final double currentThreshold;
    private final long timeThreshold;
    private long since;

    private OvercurrentMonitor(int[] channels, double currentThreshold, long timeThreshold) {
      validateChannels(channels);
      this.channels = channels;
      this.currentThreshold = currentThreshold;
      this.timeThreshold = timeThreshold;
      since = getTimeMillis();
    }

    private OvercurrentMonitor(int channel, double currentThreshold, long timeThreshold) {
      this(new int[] { channel }, currentThreshold, timeThreshold);
    }

    public boolean check() {
      for (int channel : channels) {
        if (getCurrent(channel) > currentThreshold)
          return getTimeMillis() - since > timeThreshold;
      }
      since = getTimeMillis();
      return false;
    }
  }

  /**
   * Checks if a channel of the PDP has been below the current threshold for the
   * specified period of time
   * 
   * @param channel          Channel of the PDP to check
   * @param currentThreshold Min current allowed
   * @param timeThreshold    Cutoff time in milliseconds
   */
  public class UndercurrentMonitor {
    private final int[] channels;
    private final double currentThreshold;
    private final long timeThreshold;
    private long since;

    private UndercurrentMonitor(int[] channels, double currentThreshold, long timeThreshold) {
      validateChannels(channels);
      this.channels = channels;
      this.currentThreshold = currentThreshold;
      this.timeThreshold = timeThreshold;
      since = getTimeMillis();
    }

    private UndercurrentMonitor(int channel, double currentThreshold, long timeThreshold) {
      this(new int[] { channel }, currentThreshold, timeThreshold);
    }

    public boolean check() {
      for (int channel : channels) {
        if (getCurrent(channel) < currentThreshold)
          return getTimeMillis() - since > timeThreshold;
      }
      since = getTimeMillis();
      return false;
    }
  }

  /**
   * Checks if the PDP has been below the voltage threshold for the specified
   * period of time
   * 
   * @param voltageThreshold Min voltage allowed
   * @param timeThreshold    Cutoff time in milliseconds
   */
  public class UndervoltageMonitor {
    private final double voltageThreshold;
    private final long timeThreshold;
    private long since;

    private UndervoltageMonitor(double voltageThreshold, long timeThreshold) {
      this.voltageThreshold = voltageThreshold;
      this.timeThreshold = timeThreshold;
      since = getTimeMillis();
    }

    public boolean check() {
      if (getVoltage() < voltageThreshold)
        return getTimeMillis() - since > timeThreshold;
      since = getTimeMillis();
      return false;
    }
  }

  private final int NUM_CHANNELS = 16; // 16 channels on the PDP

  private long getTimeMillis() {
    return (long) (Timer.getFPGATimestamp() * 1000);
  }

  private void validateChannel(int channel) {
    if (channel < 0 || channel >= NUM_CHANNELS)
      throw new IllegalArgumentException("Invalid channel number");
  }

  private void validateChannels(int[] channels) {
    for (int channel : channels)
      validateChannel(channel);
  }

  public OvercurrentMonitor MakeOvercurrentMonitor(int channel, double currentThreshold, int timeThreshold) {
    return new OvercurrentMonitor(channel, currentThreshold, timeThreshold);
  }

  public OvercurrentMonitor MakeOvercurrentMonitor(int[] channels, double currentThreshold, int timeThreshold) {
    return new OvercurrentMonitor(channels, currentThreshold, timeThreshold);
  }

  public UndercurrentMonitor MakeUnercurrentMonitor(int channel, double currentThreshold, int timeThreshold) {
    return new UndercurrentMonitor(channel, currentThreshold, timeThreshold);
  }

  public UndercurrentMonitor MakeUndercurrentMonitor(int[] channels, double currentThreshold, int timeThreshold) {
    return new UndercurrentMonitor(channels, currentThreshold, timeThreshold);
  }

  public UndervoltageMonitor MakeUndervoltageMonitor(double voltageThreshold, int timeThreshold) {
    return new UndervoltageMonitor(voltageThreshold, timeThreshold);
  }

}
