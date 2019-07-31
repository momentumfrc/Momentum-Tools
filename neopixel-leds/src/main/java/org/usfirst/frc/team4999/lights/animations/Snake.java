package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.Commands;
import org.usfirst.frc.team4999.lights.Packet;

import static org.usfirst.frc.team4999.lights.AnimationUtils.*;


public class Snake implements Animation {
	
	/**
	 * Make a snake with a foreground and background colors
	 * @param primary the foreground color
	 * @param background the background color
	 * @param head how many pixels long the solid head should be
	 * @param tail how many pixels long the tail should be. The tail fades into the background
	 * @param spaceBetween how many pixels of background color there are between snakes
	 * @param msBetweenFrames how often to update the animation
	 * @return the snake animation
	 */
	public static Snake twoColorSnake(Color primary, Color background, int head, int tail, int spaceBetween, int msBetweenFrames) {
		return twoColorSnake(primary, background, head, tail, spaceBetween, msBetweenFrames, false);
	}
	
	/**
	 * Make a snake with a foreground and background colors
	 * @param primary the foreground color
	 * @param background the background color
	 * @param head how many pixels long the solid head should be
	 * @param tail how many pixels long the tail should be. The tail fades into the background
	 * @param spaceBetween how many pixels of background color there are between snakes
	 * @param msBetweenFrames how often to update the animation
	 * @param reversed whether to go backwards
	 * @return the snake animation
	 */
	public static Snake twoColorSnake(Color primary, Color background, int head, int tail, int spaceBetween, int msBetweenFrames, boolean reversed) {
		Color[] snake = new Color[head+tail+spaceBetween];
		// create the head
		for(int i = 0; i < head; i++) {
			snake[i] = primary;
		}
		// create the tail
		for(int i = head; i < head+tail; i++) {
			snake[i] = blendInto(background, primary, (i+1 - head)/(double)(tail+1));
		}
		// create the background frames
		for(int i = head+tail; i < snake.length; i++) {
			snake[i] = background;
		}
		// reverse the snake if it's going in the reverse direction
		if(reversed) {
			for(int i = 0; i < snake.length/2; i++) {
				Color tmp = snake[i];
				snake[i] = snake[snake.length - i - 1];
				snake[snake.length - i - 1] = tmp;
			}
		}
		Snake s = new Snake(snake, msBetweenFrames);
		s.setReverse(reversed);
		return s;
	}
	
	/**
	 * Creates a rainbow-colored snake
	 * @param msBetweenFrames how often to update the animation
	 * @return the snake
	 */
	public static Snake rainbowSnake(int msBetweenFrames) {
		Color[] snake = {
				Color.RED,
				new Color(255,127,0),
				Color.YELLOW,
				Color.GREEN,
				Color.BLUE,
				new Color(139,0,255)
		};
		return new Snake(snake, msBetweenFrames);
	}
	
	private Color[] snakes;
	private int offset, speed, increment = 1;
	
	public Snake(Color[] snakes, int msBetweenFrames, boolean reversed) {
		this(snakes, msBetweenFrames);
		this.increment = reversed ? -1 : 1;
	}
	
	public Snake(Color[] snakes, int msBetweenFrames) {
		this.snakes = snakes;
		offset = 0;
		speed = msBetweenFrames;
	}

	@Override
	public Packet[] getNextFrame() {

		Color[] outbuff = new Color[snakes.length];
		
		for(int i = 0; i < outbuff.length; i++) {
			outbuff[i] = snakes[(i + offset) % snakes.length];
		}
		
		offset = (offset + increment + snakes.length) % snakes.length;
		
		return displayColorBuffer(outbuff);
		
	}

	@Override
	public int getFrameDelayMilliseconds() {
		return speed;
	}
	
	private static Color blendInto(Color bg, Color fg, double percentbg) {
		int r = fg.getRed() + (int)((bg.getRed() - fg.getRed()) * percentbg);
		int g = fg.getGreen() + (int)((bg.getGreen() - fg.getGreen()) * percentbg);
		int b = fg.getBlue() + (int)((bg.getBlue() - fg.getBlue()) * percentbg);
		return new Color(r,g,b);
	}
	
	public void reverse() {
		increment = -increment;
	}
	public void setReverse(boolean b) {
		increment = b ? -1 : 1;
	}

}
