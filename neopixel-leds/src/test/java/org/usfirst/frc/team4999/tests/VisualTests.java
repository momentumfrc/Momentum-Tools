package org.usfirst.frc.team4999.tests;

import org.junit.Test;
import org.usfirst.frc.team4999.lights.*;
import org.usfirst.frc.team4999.lights.animations.*;
import org.usfirst.frc.team4999.tools.*;

public class VisualTests {

    private static Color[] rainbowcolors = {
        new Color(139, 0, 255),
        Color.BLUE, Color.GREEN,
        Color.YELLOW,
        new Color(255, 127, 0),
        Color.RED
    };
    
    @Test
    public void testIndexingAnimation() {
        SwingDisplay display = new SwingDisplay(80);
        Animator animator = new Animator(display);
        
        Color[] indexColors = new Color[10];
        for(int i = 0; i < 5; i++) {
            indexColors[i] = Color.WHITE;
        }
        for(int i = 5; i < 10; i++) {
            indexColors[i] = Color.BLACK;
        }

        Animation indexingAnimation = new Solid(indexColors);
        
        animator.setAnimation(indexingAnimation);

        display.sleep(5000);

        display.close();
        animator.stopAnimation();
    }

    @Test
    public void testRainbowAnimation() {
        SwingDisplay display = new SwingDisplay(80);
        Animator animator = new Animator(display);

        Animation rainbow = new AnimationSequence(new Animation[] {
            Snake.rainbowSnake(70),
            Fade.rainbowFade(100, 20),
            new Bounce(Color.WHITE, rainbowcolors, 50, 50),
            new Stack(rainbowcolors, 50, 40),
            new BounceStack(rainbowcolors, 14, 40) },
            new int[] { 5000, 5000, 10000, 10000, 10000 }
        );

        animator.setAnimation(rainbow);

        display.sleep(50000);

        display.close();
        animator.stopAnimation();
    }
}
