package org.usfirst.frc.team4999.lights.animations;

import org.usfirst.frc.team4999.lights.Packet;

public class AnimationSequence implements Animation {
	
	private Animation[] animations;
	private int[] times;
	
	private long startTime;
	private int currentidx = 0;
	
	/**
	 * Loops through a series of animations
	 * @param animations the animations to run
	 * @param time the time to wait for every animation
	 */
	public AnimationSequence(Animation[] animations, int time) {
		int[] times = new int[animations.length];
		for(int i = 0; i < times.length; i++) {
			times[i] = time; 
		}
		
		startTime = System.currentTimeMillis();
		
		this.animations = animations;
		this.times = times;
	}
	
	/**
	 * Loops through a series of animations
	 * @param animations the animations to run
	 * @param the time to wait for each animation. Should be equal in size to animations[]
	 */
	public AnimationSequence(Animation[] animations, int[] times) {
		if (animations.length != times.length)
			throw new IllegalArgumentException("Each animation must have a time set");
		
		startTime = System.currentTimeMillis();
		
		this.animations = animations;
		this.times = times;
	}

	@Override
	public Packet[] getNextFrame() {
		if(System.currentTimeMillis() - startTime >= times[currentidx]) {
			startTime = System.currentTimeMillis();
			currentidx = (currentidx + 1) % times.length;
		}
		return animations[currentidx].getNextFrame();
	}

	@Override
	public int getFrameDelayMilliseconds() {
		
		int delay = animations[currentidx].getFrameDelayMilliseconds();
		
		// If the animation wants to delay indefinitely, instead delay for the length of this animation's duration
		delay = (delay < 0) ? times[currentidx] : delay;
		
		return delay;
	}

}
