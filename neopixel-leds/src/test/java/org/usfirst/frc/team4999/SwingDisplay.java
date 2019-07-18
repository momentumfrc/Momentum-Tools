package org.usfirst.frc.team4999;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.usfirst.frc.team4999.lights.BrightnessFilter;
import org.usfirst.frc.team4999.lights.Display;
import org.usfirst.frc.team4999.lights.Packet;

public class SwingDisplay implements Display {

	private class TestComponent extends JComponent implements Display {

		private static final long serialVersionUID = -1282530564629193895L;
	
		private final int PIXEL_SIZE = 20;
		private final int TXT_OFFSET = 4;
		
		Color[] pixels;
		
		Dimension pixsize;
		
		private long lastTime;
		
		public TestComponent(int numPixels) {
			super();
			pixels = new Color[numPixels];
			for(int i = 0; i < pixels.length; i++) {
				pixels[i] = new Color(255,255,255);
			}
			resize();
			lastTime = System.currentTimeMillis();
		}
		
		@Override
		public void paintComponent(Graphics gd) {
			Graphics2D g = (Graphics2D) gd;
			g.setPaint(Color.BLACK);
			for(int i = 0; i < pixels.length; i++) {
				Rectangle rect = new Rectangle(i * PIXEL_SIZE, 0, PIXEL_SIZE, PIXEL_SIZE);
				String num = Integer.toString(i);
				g.draw(rect);
				g.drawString(num, i * PIXEL_SIZE + TXT_OFFSET, PIXEL_SIZE - TXT_OFFSET);
			}
			for(int i = 0; i < pixels.length; i++) {
				Rectangle rect = new Rectangle(i * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
				g.setPaint(pixels[i]);
				g.fill(rect);
			}
		}
		
		public void resize() {
			setPreferredSize(new Dimension(pixels.length*PIXEL_SIZE, PIXEL_SIZE * 2));
		}
		
		private int unsignedByteValue(byte b) {
			// See https://stackoverflow.com/questions/11380062/what-does-value-0xff-do-in-java
			// Basically, when the byte is cast to an int, it is sign-extended.
			// By masking only the last byte, we remove the sign-extended bits.
			return ((int) b) & 0xff;
		}
		
		@Override
		public void show(Packet[] pixels) {

			double packetPerSec = pixels.length / ((System.currentTimeMillis() - lastTime) / 1000.0);
			lastTime = System.currentTimeMillis();
			if(packetPerSec > 781.25) {
				System.out.format("Exceeds data limit! %.2f packets/sec\n",packetPerSec);
			}

			int start, length, repeat, totallength;
			
			for(Packet packet : pixels) {
				byte[] b = packet.getData();
				Color c;
				switch(b[1]) {
				case 0x02:
					this.pixels[unsignedByteValue(b[2])] = new Color(unsignedByteValue(b[3]), unsignedByteValue(b[4]), unsignedByteValue(b[5]));
					break;
				case 0x03:
					c = new Color(unsignedByteValue(b[3]), unsignedByteValue(b[4]), unsignedByteValue(b[5]));
					for(int i = unsignedByteValue(b[2]); i < Math.min(unsignedByteValue(b[2])+unsignedByteValue(b[6]), this.pixels.length); i++) {
						this.pixels[i] = c;
					}
					break;
				case 0x04:
					c = new Color(unsignedByteValue(b[3]), unsignedByteValue(b[4]), unsignedByteValue(b[5]));
					start = unsignedByteValue(b[2]);
					length = unsignedByteValue(b[6]);
					repeat = unsignedByteValue(b[7]);
					for(int r = 0; r < this.pixels.length; r += repeat) {
						for(int i = r+start; i < r+start+length && i < this.pixels.length; i++) {
							this.pixels[i] = c;
						}
					}
					
					break;
				case 0x05:
					c = new Color(unsignedByteValue(b[3]), unsignedByteValue(b[4]), unsignedByteValue(b[5]));
					start = unsignedByteValue(b[2]);
					length = unsignedByteValue(b[6]);
					repeat = unsignedByteValue(b[7]);
					totallength = unsignedByteValue(b[8]);
					for(int r = 0; r < start + totallength && r < this.pixels.length; r += repeat) {
						for(int i = r+start; i < start + totallength && i < r+start+length && i < this.pixels.length; i++) {
							this.pixels[i] = c;
						}
					}
					break;
				default:
					break;
				}
			}
			
			resize();
			
			revalidate();
			repaint();
		}
		
	}

    private TestComponent component;

	private Object lock = new Object();

	boolean visible = false;

    public SwingDisplay(int numPixels) {

		BrightnessFilter.setBrightness(1);

        component = new TestComponent(numPixels);

        JFrame frame = new JFrame();
		frame.add(component);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		frame.pack();
		frame.setVisible(true);
		visible = true;

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg) {
				synchronized(lock) {
					frame.setVisible(false);
					lock.notifyAll();;
					visible = false;
				}
			}
		});
        
    }

    @Override
    public void show(Packet[] commands) {
        component.show(commands);
	}
	
	public void waitForClose() {
		synchronized(lock) {
			try {
				lock.wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isVisible(){
		return visible;
	}
}
