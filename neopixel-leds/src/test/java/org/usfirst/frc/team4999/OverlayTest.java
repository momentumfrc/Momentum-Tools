package org.usfirst.frc.team4999;

import org.junit.Test;
import org.usfirst.frc.team4999.lights.AnimationCoordinator;
import org.usfirst.frc.team4999.lights.Animator;
import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.animations.*;


public class OverlayTest {

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFader() {
        SwingDisplay display = new SwingDisplay(80);

        Animation fader = new Fade(new Color[] {Color.RED, Color.YELLOW, Color.BLUE}, 1000, 100, 5, 25);
        Animation background = Snake.rainbowSnake(50);

        Animation overlayed = new Overlay(new Animation[] {background, fader});

        Animator an = new Animator(display);

        an.setAnimation(overlayed);

        display.waitForClose();

        an.stopAnimation();
    }

    @Test
    public void testCoordinator() {
        SwingDisplay display = new SwingDisplay(80);
        Animator an = new Animator(display);
        AnimationCoordinator coord = new AnimationCoordinator();

        Animation background = new BounceStack(new Color[] {Color.MOMENTUM_PURPLE, Color.MOMENTUM_PURPLE, Color.MOMENTUM_BLUE, Color.MOMENTUM_BLUE}, 8, 40);

        coord.setBase(background);
        an.setAnimation(coord.getCurrentAnimation());
        sleep(5000);

        Animation greenSection = new SolidPatch(Color.GREEN, 15, 30);
        coord.pushAnimation("Green Section", greenSection, true);
        an.setAnimation(coord.getCurrentAnimation());
        sleep(5000);

        Animation rainbow = Snake.rainbowSnake(50);
        coord.pushAnimation("RainbowSnake", rainbow, false);
        an.setAnimation(coord.getCurrentAnimation());
        sleep(5000);

        Animation fader = new Fade(new Color[] {Color.RED, Color.YELLOW, Color.BLUE}, 1000, 100, 5, 25);
        coord.pushAnimation("Fader", fader, true);
        an.setAnimation(coord.getCurrentAnimation());
        sleep(5000);

        coord.popAnimation("Green Section");
        coord.pushAnimation("Green Section", greenSection, true);
        an.setAnimation(coord.getCurrentAnimation());
        sleep(5000);

        coord.setBase(new Solid(Color.RED));
        an.setAnimation(coord.getCurrentAnimation());
        sleep(5000);

        coord.popAnimation("Fader");
        coord.popAnimation("Green Section");
        coord.popAnimation("RainbowSnake");
        an.setAnimation(coord.getCurrentAnimation());
        sleep(5000);

        an.stopAnimation();
    }
}
