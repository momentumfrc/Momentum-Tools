package org.usfirst.frc.team4999.lights;

import java.util.Vector;

public class AnimationUtils {
    
    private static Vector<Packet> packetBuffer = new Vector<Packet>();
    public static Packet[] displayColorBuffer(Color[] buffer) {
        packetBuffer.clear();

		Color curr = buffer[0];
		int addr = 0;
		int len = 1;
		for(int i = 1; i < buffer.length; i++) {
			if(!curr.equals(buffer[i])){
				packetBuffer.add(Commands.makeStride(addr, curr, len, buffer.length));
				curr = buffer[i];
				addr = i;
				len = 1;
			} else {
				len++;
			}
		}
		packetBuffer.add(Commands.makeStride(addr, curr, len, buffer.length));
		
		
		Packet[] out = new Packet[packetBuffer.size()];
		
		
		for(int i = 0; i < packetBuffer.size(); i++) {
			out[i] = packetBuffer.get(i);
		}
		
		return out;
    }
}
