package org.usfirst.frc.team4999.tools;

import org.usfirst.frc.team4999.lights.*;
import org.usfirst.frc.team4999.lights.animations.Animation;
import org.usfirst.frc.team4999.lights.animations.Solid;

public class TestAnimator extends Animator {
	
    private Animation current;
    public final UnitTestDisplay display;
	
	/**
	 * Creates an animator using the specified {@link Display} 
	 * @param pixels Display to output to
	 */
	public TestAnimator(int numPixels) {
        super(null);
        display = new UnitTestDisplay(numPixels);
        setAnimation(new Solid(Color.BLACK));
	}
	
	/**
	 * Set the animation run on the AnimationThread
	 * @param newAnimation
	 */
    @Override
	public void setAnimation(Animation newAnimation) {
		if(newAnimation == null) {
			System.out.println("Recieved null animation! Defaulting to solid black");
			current = new Solid(Color.BLACK);
		} else {
            current = newAnimation;
        }
    }
    
    public void displayFrames(int numFrames) {
        for(int i = 0; i < numFrames; i++) {
			display.show(current.getNextFrame());
			int delay = current.getFrameDelayMilliseconds();
			
			if(delay < 0 ) System.out.println("Animation returned a delay less than 0... interpreting as no delay");
			if(display.window.isVisible()) {
                if (delay > 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }
    }

    public void stepFrames(int numFrames) {
        for(int i = 0; i < numFrames; i++) {
			display.show(current.getNextFrame());
			int delay = current.getFrameDelayMilliseconds();
			
			if(delay < 0 ) System.out.println("Animation returned a delay less than 0... interpreting as no delay");
			if(display.window.isVisible()) {
                if (delay > 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                display.window.waitForKeypress();
            }
        }
    }
}
