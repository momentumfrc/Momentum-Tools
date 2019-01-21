package org.usfirst.frc.team4999.lights;

public class Commands {
	
	// All according to specification
	private static final byte DISPLAY_FRAME = 0x01;
	private static final byte SET_SINGLE = 0x02;
	private static final byte SET_RUN = 0x03;
	private static final byte SET_STRIDE = 0x04;
	
	private static byte[] setSizeByte(byte[] data) {
		byte[] out = new byte[data.length + 1];
		out[0] = (byte) (data.length);
		for(int i = 0; i < data.length; i++) {
			out[i+1] = data[i];
		}
		return out;
	}
	
	/**
	 * Makes a packet to set a single LED
	 * @param address LED to set
	 * @param color desired color
	 * @return the packet
	 */
	public static Packet makeSingle(int address, Color color) {
		Color dimColor = BrightnessFilter.dimColor(color);
		return makeSingleNoDim(address, dimColor);
	}
	
	/**
	 * Makes a packet to set a single LED without dimming the input color
	 * @param address LED to set
	 * @param color desired color
	 * @return the packet
	 */
	public static Packet makeSingleNoDim(int address, Color color) {
		byte[] data = {
				SET_SINGLE,
				(byte) address, 
				(byte) color.getRed(),
				(byte) color.getGreen(),
				(byte) color.getBlue(),
		};
		return new Packet(setSizeByte(data));
	}
	
	/**
	 * Makes a packet to set a run of LEDs
	 * @param address LED to set
	 * @param color desired color
	 * @param length number of LEDs to set
	 * @return the packet
	 */
	public static Packet makeRun(int address, Color color, int length) {
		Color dimColor = BrightnessFilter.dimColor(color);
		return makeRunNoDim(address, dimColor, length);
	}
	
	/**
	 * Makes a packet to set a run of LEDs without dimming the input color
	 * @param address LED to set
	 * @param color desired color
	 * @param length number of LEDs to set
	 * @return the packet
	 */
	public static Packet makeRunNoDim(int address, Color color, int length) {
		byte[] data = {
				SET_RUN,
				(byte) address, 
				(byte) color.getRed(),
				(byte) color.getGreen(),
				(byte) color.getBlue(),
				(byte) length
		};
		return new Packet(setSizeByte(data));
	}
	
	/**
	 * Makes a packet to set a run of LEDs and repeat that run every stride LEDs
	 * @param address LED to set
	 * @param color desired color
	 * @param length number of LEDs to set
	 * @param stride how often to repeat the run
	 * @return the packet
	 */
	public static Packet makeStride(int address, Color color, int length, int stride) {
		Color dimColor = BrightnessFilter.dimColor(color);
		return makeStrideNoDim(address, dimColor, length, stride);
	}
	
	/**
	 * Makes a packet to set a run of LEDs and repeat that run every stride LEDs, without dimming the input color
	 * @param address LED to set
	 * @param color desired color
	 * @param length number of LEDs to set
	 * @param stride how often to repeat the run
	 * @return the packet
	 */
	public static Packet makeStrideNoDim(int address, Color color, int length, int stride) {
		byte[] data = {
				SET_STRIDE,
				(byte) address, 
				(byte) color.getRed(),
				(byte) color.getGreen(),
				(byte) color.getBlue(),
				(byte) length,
				(byte) stride
		};
		return new Packet(setSizeByte(data));
	}
	
	/**
	 * Makes a sync packet to sync packets with the I2C slave
	 * @return the packet
	 */
	public static Packet makeSyncPacket() {
		byte[] data = new byte[16];
		for(int i = 0; i < data.length; i++) {
			data[i] = (byte) (0xFF);
		}
		return new Packet(data);
	}
	
	/**
	 * Makes a packet to display the current frame
	 * @return the packet
	 */
	public static Packet makeShowPacket() {
		byte[] data = {DISPLAY_FRAME};
		return new Packet(setSizeByte(data));
	}
	
}
