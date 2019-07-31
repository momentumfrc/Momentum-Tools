package org.usfirst.frc.team4999.lights;

public class Commands {
	
	// All according to specification
	private static final byte DISPLAY_FRAME = 0x01;
	private static final byte SET_SINGLE = 0x02;
	private static final byte SET_RUN = 0x03;
	private static final byte SET_STRIDE = 0x04;
	private static final byte SET_STRIDE_RANGED = 0x05;

	private static byte intToByte(int in) {
		if(in > 255 || in < 0) {
			System.out.format("Cannot fit %d in one byte!\n", in);
			throw new IllegalArgumentException("Input must fit in one unsigned byte");
		}
		return (byte) in;
	}

	private static final int byteToInt(byte in) {
		return ((int) in) & 0xFF;
	}
	
	private static byte[] setSizeByte(byte[] data) {
		byte[] out = new byte[data.length + 1];
		out[0] = intToByte(data.length);
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
				intToByte(address), 
				intToByte(color.getRed()),
				intToByte(color.getGreen()),
				intToByte(color.getBlue())
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
				intToByte(address), 
				intToByte(color.getRed()),
				intToByte(color.getGreen()),
				intToByte(color.getBlue()),
				intToByte(length)
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
				intToByte(address), 
				intToByte(color.getRed()),
				intToByte(color.getGreen()),
				intToByte(color.getBlue()),
				intToByte(length),
				intToByte(stride)
		};
		return new Packet(setSizeByte(data));
	}

	/**
	 * Makes a packet to set a run of LEDs and repeat that run every stride LEDs, stopping after totallength LEDs
	 * @param startaddress
	 * @param color
	 * @param length
	 * @param stride
	 * @param totallength
	 * @return
	 */
	public static Packet makeStrideWithEnd(int startaddress, Color color, int length, int stride, int totallength) {
		Color dimColor = BrightnessFilter.dimColor(color);
		return makeStrideWithEndNoDim(startaddress, dimColor, length, stride, totallength);
	}

	/**
	 * Makes a packet to set a run of LEDs and repeat that run every stride LEDs, stopping after totallength LEDs
	 * @param startaddress
	 * @param color
	 * @param length
	 * @param stride
	 * @param totallength
	 * @return
	 */
	public static Packet makeStrideWithEndNoDim(int startaddress, Color color, int length, int stride, int totallength) {
		byte[] data = {
				SET_STRIDE_RANGED,
				intToByte(startaddress),
				intToByte(color.getRed()),
				intToByte(color.getGreen()),
				intToByte(color.getBlue()),
				intToByte(length),
				intToByte(stride),
				intToByte(totallength)
		};
		return new Packet(setSizeByte(data));
	}

	// this is probably not the best place for this, but I couldn't think of anywhere better
	public static Packet[] clipPacketRange(Packet oldpacket, int startaddress, int totallength) {
		byte[] packetdata = oldpacket.getData();
		int oldstartaddress, length, stride, oldtotallength;
		int newstartaddress, newtotallength;
		Color color;

		oldstartaddress = byteToInt(packetdata[2]);
		color = new Color(byteToInt(packetdata[3]), byteToInt(packetdata[4]), byteToInt(packetdata[5]));
		switch(byteToInt(packetdata[1])) {
			case SET_SINGLE:
				if(oldstartaddress < startaddress || oldstartaddress >= startaddress + totallength) {
					return null;
				} else {
					return new Packet[] {oldpacket};
				}
			case SET_RUN:
				length = byteToInt(packetdata[6]);
				if(oldstartaddress < startaddress)
					oldstartaddress = startaddress;
				if(oldstartaddress + length > startaddress + totallength)
					length = startaddress + totallength - oldstartaddress;	
				
				return new Packet[] {Commands.makeRunNoDim(oldstartaddress, color, length)};
			case SET_STRIDE:
				length = byteToInt(packetdata[6]);
				stride = byteToInt(packetdata[7]);

				if(stride == 0) {
					return Commands.clipPacketRange(Commands.makeRunNoDim(oldstartaddress, color, length), startaddress, totallength);
				}

				Packet clippedRegion = null;
				if(oldstartaddress < startaddress && oldstartaddress + length > startaddress) {
					clippedRegion = Commands.clipPacketRange(Commands.makeRun(startaddress, color, oldstartaddress + length - startaddress), startaddress, totallength)[0];
				}

				newstartaddress = startaddress + ( (stride - (startaddress % stride) + oldstartaddress) % stride);

				newtotallength = startaddress + totallength - newstartaddress;

				if(clippedRegion != null) {
					return new Packet[] {clippedRegion, Commands.makeStrideWithEndNoDim(newstartaddress, color, length, stride, newtotallength)};
				} else {
					return new Packet[] {Commands.makeStrideWithEndNoDim(newstartaddress, color, length, stride, newtotallength)};
				}
			case SET_STRIDE_RANGED:
				length = byteToInt(packetdata[6]);
				stride = byteToInt(packetdata[7]);
				oldtotallength = byteToInt(packetdata[8]);

				int oldstopaddress = oldstartaddress + oldtotallength;
				int newstopaddress = startaddress + totallength;

				oldstartaddress = startaddress + ( (stride - (startaddress % stride) + oldstartaddress) % stride);

				oldtotallength = Math.min(oldstopaddress, newstopaddress) - oldstartaddress;

				return new Packet[] {Commands.makeStrideWithEndNoDim(oldstartaddress, color, length, stride, oldtotallength)};
			default:
				throw new IllegalArgumentException("Invalid packet command");
		}
	}
	
	/**
	 * Makes a sync packet to sync packets with the I2C slave
	 * @return the packet
	 */
	public static Packet makeSyncPacket() {
		byte[] data = new byte[16];
		for(int i = 0; i < data.length; i++) {
			data[i] = intToByte((0xFF));
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
