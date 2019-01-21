package org.usfirst.frc.team4999.lights;

/**
 * Output to display animation data
 * <p>
 * Display receives an array of Packets which match <a href="https://github.com/momentumfrc/2017Steamworks/wiki/Light-strings-on-the-Robot">this</a> structure 
 * @author jordan
 *
 */
public interface Display {
	/**
	 * Display the packets
	 * @param pixels
	 */
	void show(Packet[] commands);
}
