package org.usfirst.frc.team4999.tests;

import org.junit.Test;
import org.usfirst.frc.team4999.tools.*;
import org.usfirst.frc.team4999.lights.animations.*;
import org.usfirst.frc.team4999.lights.*;

import static org.junit.Assert.assertTrue;


public class AnimationTest {

    private static Color[] rainbowcolors = {
        new Color(139, 0, 255),
        Color.BLUE,
        Color.GREEN,
        Color.YELLOW,
        new Color(255, 127, 0),
        Color.RED
    };

    @Test
    public void testBlinkAnimation() {
        TestAnimator animator = new TestAnimator(80);

        Animation blink = new Blink(rainbowcolors, 500);

        animator.setAnimation(blink);

        animator.displayFrames(10);

        animator.display.window.close();

        //animator.display.writeToFile("BlinkAnimation.bin");
        assertTrue(animator.display.compareToFile("BlinkAnimation.bin"));
    }

    @Test
    public void testBounceAnimation() {
        TestAnimator animator = new TestAnimator(80);

        Animation bounce = new Bounce(Color.WHITE, rainbowcolors, 45, 20);

        animator.setAnimation(bounce);

        animator.displayFrames(250);
        
        animator.display.window.close();

        //animator.display.writeToFile("BounceAnimation.bin");
        assertTrue(animator.display.compareToFile("BounceAnimation.bin"));
    }

    @Test
    public void testBounceStackAnimation()  {
        TestAnimator animator = new TestAnimator(80);

        Animation bounceStack = new BounceStack(rainbowcolors, 14, 20);
        
        animator.setAnimation(bounceStack);

        animator.displayFrames(250);

        animator.display.window.close();

        //animator.display.writeToFile("BounceStackAnimation.bin");
        assertTrue(animator.display.compareToFile("BounceStackAnimation.bin"));
    }

    @Test
    public void testFadeAnimation() {
        TestAnimator animator = new TestAnimator(80);

        Animation fade = new Fade(rainbowcolors, 300, 300);

        animator.setAnimation(fade);

        animator.displayFrames(600);

        animator.display.window.close();

        //animator.display.writeToFile("FadeAnimation.bin");
        assertTrue(animator.display.compareToFile("FadeAnimation.bin"));
    }

    @Test
    public void testSnakeAnimation() {
        TestAnimator animator = new TestAnimator(80);

        Animation snake = Snake.twoColorSnake(Color.BLUE, Color.WHITE, 1, 5, 10, 20);
        
        animator.setAnimation(snake);

        animator.displayFrames(150);

        animator.display.window.close();
        
        //animator.display.writeToFile("TwoColorSnakeAnimation.bin");
        assertTrue(animator.display.compareToFile("TwoColorSnakeAnimation.bin"));
    }

    @Test
    public void testSolidAnimation() {
        TestAnimator animator = new TestAnimator(80);

        Animation solid = new Solid(rainbowcolors);

        animator.setAnimation(solid);

        animator.displayFrames(4);

        animator.display.window.close();

        //animator.display.writeToFile("SolidAnimation.bin");
        assertTrue(animator.display.compareToFile("SolidAnimation.bin"));
    }

    @Test
    public void testStackAnimation() {
        TestAnimator animator = new TestAnimator(80);

        Animation stack = new Stack(rainbowcolors, 25, 40);
        
        animator.setAnimation(stack);

        animator.displayFrames(625);

        animator.display.window.close();
        
        //animator.display.writeToFile("StackAnimation.bin");
        assertTrue(animator.display.compareToFile("StackAnimation.bin"));
    }

    @Test
    public void testOverlayAnimation() {
        TestAnimator animator = new TestAnimator(80);

        Animation bouncer = new ClippedAnimation(new BounceStack(new Color[]{Color.BLACK, Color.BLACK, Color.BLACK}, 14, 20), 5, 25);
        Animation background = Snake.rainbowSnake(60);

        Animation overlayed = new Overlay(new Animation[] {background, bouncer});

        animator.setAnimation(overlayed);

        animator.displayFrames(250);
        animator.display.window.close();

        //animator.display.writeToFile("OverlayAnimation.bin");
        assertTrue(animator.display.compareToFile("OverlayAnimation.bin"));
    }

    @Test
    public void testClippedAnimation() {
        TestAnimator animator = new TestAnimator(80);
        
        Animation base = Snake.rainbowSnake(300);
        Animation fade = new Fade(new Color[] {Color.RED, Color.YELLOW, Color.BLUE}, 2000, 0);
        Animation clippedFade = new ClippedAnimation(fade, 0, 30);
        Animation solid = new Solid(new Color[] {Color.GREEN, Color.BLUE});
        Animation clippedSolid = new ClippedAnimation(solid, 50, 20);

        Animation overlay = new Overlay(new Animation[] {base, clippedFade, clippedSolid});

        animator.setAnimation(overlay);

        animator.displayFrames(300);

        animator.display.window.close();
        
        //animator.display.writeToFile("ClippedAnimation.bin");
        assertTrue(animator.display.compareToFile("ClippedAnimation.bin"));
    }
}
