package org.usfirst.frc.team4999.lights;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;

public class AddressableLEDDisplay implements Display {
    private final AddressableLED leds;
    private final AddressableLEDBuffer buffer;

    private final static int TEST_PATTERN_BRIGHTNESS = 200;

    public AddressableLEDDisplay(int pwm, int length) {
        leds = new AddressableLED(pwm);
        buffer = new AddressableLEDBuffer(length);

        leds.setLength(buffer.getLength());

        showTestPattern();

        leds.start();
    }

    private void showTestPattern() {
        int length = buffer.getLength();
        for(int i = 0; i < length; i++) {
            buffer.setHSV(i, (i * 180) / length, 255, TEST_PATTERN_BRIGHTNESS);
        }
        leds.setData(buffer);
    }

    private int unsignedByteValue(byte b) {
        // See https://stackoverflow.com/questions/11380062/what-does-value-0xff-do-in-java
        // The fundamental issue is that byte values are signed.
        // So when the byte is cast to an int, it is sign-extended.
        // For example, take the unsigned value 255 = 0b10000000.
        // When interpreted using 2's complement, 0b10000000 = -128.
        // However, when the byte is cast to int
        // 0b10000000 becomes 0b111111111111111111111111100000000
        // since integers are 4 bytes long.
        // This preserves the 2's complement value -128 through a process called sign-extension.
        // However, by then masking only the last byte, we remove the sign-extended bits.
        // 0b111111111111111111111111100000000 & 0b00000000000000000000000011111111
        // -> 0b000000000000000000000000100000000
        // When you interpret 0b000000000000000000000000100000000 using 2's complement, you get 255.
        return ((int) b) & 0xff;
    }

	@Override
	public void show(Packet[] commands) {
        
        int start, length, repeat, totallength;

        int bufflength = buffer.getLength();

		for(Packet packet : commands) {
            byte[] b = packet.getData();
            Color c;
            switch(b[1]) {
            case 0x02:
                buffer.setRGB(unsignedByteValue(b[2]), unsignedByteValue(b[3]), unsignedByteValue(b[4]), unsignedByteValue(b[5]));
                break;
            case 0x03:
                c = new Color(unsignedByteValue(b[3]), unsignedByteValue(b[4]), unsignedByteValue(b[5]));
                for(int i = unsignedByteValue(b[2]); i < Math.min(unsignedByteValue(b[2])+unsignedByteValue(b[6]), bufflength); i++) {
                    buffer.setLED(i, c);
                }
                break;
            case 0x04:
                c = new Color(unsignedByteValue(b[3]), unsignedByteValue(b[4]), unsignedByteValue(b[5]));
                start = unsignedByteValue(b[2]);
                length = unsignedByteValue(b[6]);
                repeat = unsignedByteValue(b[7]);
                for(int r = 0; r < bufflength; r += repeat) {
                    for(int i = r+start; i < r+start+length && i < bufflength; i++) {
                        buffer.setLED(i, c);
                    }
                }
                
                break;
            case 0x05:
                c = new Color(unsignedByteValue(b[3]), unsignedByteValue(b[4]), unsignedByteValue(b[5]));
                start = unsignedByteValue(b[2]);
                length = unsignedByteValue(b[6]);
                repeat = unsignedByteValue(b[7]);
                totallength = unsignedByteValue(b[8]);
                for(int r = 0; r < start + totallength && r < bufflength; r += repeat) {
                    for(int i = r+start; i < start + totallength && i < r+start+length && i < bufflength; i++) {
                        buffer.setLED(i, c);
                    }
                }
                break;
            default:
                break;
            }
        }

        leds.setData(buffer);
    }
    
    public void stop() {
        leds.stop();
    }
}
