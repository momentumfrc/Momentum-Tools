package org.usfirst.frc.team4999;

import org.junit.Test;
import org.usfirst.frc.team4999.lights.Animator;
import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.animations.*;


public class OverlayTest {

    @Test
    public void testBlink() {
        SwingDisplay display = new SwingDisplay(80);

        Animation fader = new Fade(new Color[] {Color.RED, Color.YELLOW, Color.BLUE}, 1000, 100, 5, 25);
        Animation background = Snake.rainbowSnake(50);

        Animation overlayed = new Overlay(new Animation[] {background, fader});

        Animator an = new Animator(display);

        an.setAnimation(overlayed);

        display.waitForClose();

        an.stopAnimation();
    }
}
