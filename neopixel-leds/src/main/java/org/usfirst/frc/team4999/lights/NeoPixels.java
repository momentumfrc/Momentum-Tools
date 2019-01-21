package org.usfirst.frc.team4999.lights;


import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.I2C;

class NeoPixelsIO extends I2C {
	
	private final ByteBuffer buffer;
	
	private final int MAX_PACKET_SIZE = 16;
	
	public NeoPixelsIO(Port port, int deviceAddress) {
		super(port, deviceAddress);
		buffer = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);
	}
	
	public boolean writePacket(Packet packet) {
		buffer.rewind();
		buffer.put(packet.getData());
		buffer.rewind();
		return writeBulk(buffer, packet.getSize());
	}
	
}

/**
 * Class to communicate with an arduino driving a strip of NeoPixel LEDs over I2C
 * @author jordan
 *
 */
public class NeoPixels implements Display {
	
	private NeoPixelsIO strip;
	
	private static NeoPixels instance;
	
	private final int I2C_ADDRESS = 16;
	
	private final int SYNC_FREQ = 1000;
	private int syncidx = 0;
	
	Packet syncPacket, showPacket;
	
	/**
	 * Gets an instance of NeoPixels
	 * @return an instance of NeoPixels
	 */
	public static NeoPixels getInstance() {
		if(instance == null) {
			instance = new NeoPixels();
		}
		return instance;
	}
	
	private NeoPixels() {
		strip = new NeoPixelsIO(I2C.Port.kOnboard, I2C_ADDRESS);
		
		syncPacket = Commands.makeSyncPacket();
		showPacket = Commands.makeShowPacket();
	}
	
	synchronized public void show(Packet[] commands) {
		try {
			// Send a sync packet every SYNC_FREQ frames
			if(++syncidx >= SYNC_FREQ) {
				strip.writePacket(syncPacket);
				syncidx = 0;
			}
			
			// Send each packet
			for(Packet packet : commands) {
				strip.writePacket(packet);
			}
			// Show the sent packets
			strip.writePacket(showPacket);
			
		} catch (Exception e) {
			// The generic try-catch prevents an error in the purely cosmetic neopixels from killing the whole robot
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
		
	}

}
