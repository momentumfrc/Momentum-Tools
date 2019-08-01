package org.usfirst.frc.team4999.tests;

import org.junit.Test;
import org.usfirst.frc.team4999.tools.*;
import org.usfirst.frc.team4999.lights.AnimationCoordinator;
import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.animations.*;

import static org.junit.Assert.assertTrue;

public class CoordinatorTest {

    @Test
    public void testCoordinator() {
        TestAnimator an = new TestAnimator(80);
        AnimationCoordinator coord = new AnimationCoordinator(an);

        Animation background = new BounceStack(new Color[] {Color.MOMENTUM_PURPLE, Color.MOMENTUM_PURPLE, Color.MOMENTUM_BLUE, Color.MOMENTUM_BLUE}, 8, 40);

        coord.popAnimation("DNE Animation");

        coord.pushAnimation("Background", background, 1, false);
        an.displayFrames(50);

        Animation greenSection = new ClippedAnimation(new Solid(Color.GREEN), 15, 30);
        coord.pushAnimation("Green Section", greenSection, 10, true);
        an.displayFrames(50);

        Animation rainbow = Snake.rainbowSnake(50);
        coord.pushAnimation("RainbowSnake", rainbow, 100, false);
        an.displayFrames(50);

        Animation fader = new ClippedAnimation(new Fade(new Color[] {Color.RED, Color.YELLOW, Color.BLUE}, 1000, 100), 5, 25);
        coord.pushAnimation("Fader", fader, 500, true);
        an.displayFrames(50);

        coord.popAnimation("Green Section");
        coord.pushAnimation("Green Section", greenSection, 700, true);
        an.displayFrames(50);

        coord.popAnimation("Background");
        coord.pushAnimation("Background", new Solid(Color.RED), 1, false);
        an.displayFrames(50);

        coord.popAnimation("DNE Animation");

        coord.popAnimation("Fader");
        coord.popAnimation("Green Section");
        coord.popAnimation("RainbowSnake");
        an.displayFrames(10);

        an.display.window.close();
        
        //an.display.writeToFile("Coordinator.bin");
        assertTrue(an.display.compareToFile("Coordinator.bin"));
    }

    @Test
    public void testCoordinatorPriority() {
        TestAnimator an = new TestAnimator(80);
        AnimationCoordinator coord = new AnimationCoordinator(an);
        an.displayFrames(6);

        Animation solid_blue = new Solid(Color.BLUE);
        coord.pushAnimation("Solid Blue", solid_blue, 1, false);
        an.displayFrames(6);

        Animation solid_rainbow = Solid.rainbow();
        coord.pushAnimation("Rainbow", solid_rainbow, 10, false);
        an.displayFrames(6);

        Animation solid_green = new Solid(Color.GREEN);
        coord.pushAnimation("Solid Green", solid_green, 1, false);

        Animation stack = new Stack(new Color[]{Color.GREEN, Color.BLUE, Color.RED}, 10, 200);
        coord.pushAnimation("Stack", stack, 5, false);
        an.displayFrames(6);
        
        Animation rainbow_overlay = new ClippedAnimation(Snake.rainbowSnake(200), 5, 25);
        coord.pushAnimation("Rainbow overlay", rainbow_overlay, 20, true);
        an.displayFrames(25);

        coord.popAnimation("Rainbow");
        an.displayFrames(25);

        coord.popAnimation("Stack");
        an.displayFrames(25);


        an.display.window.close();
        
        //an.display.writeToFile("CoordinatorPriorities.bin");
        assertTrue(an.display.compareToFile("CoordinatorPriorities.bin"));
    }

    @Test
    public void testCoordinatorFalseTransparency() {
        TestAnimator an = new TestAnimator(80);
        AnimationCoordinator coord = new AnimationCoordinator(an);
        an.displayFrames(6);

        Animation solid_blue = new Solid(Color.BLUE);
        coord.pushAnimation("Solid Blue", solid_blue, 1, true);
        an.displayFrames(6);

        Animation rainbow_overlay = new ClippedAnimation(Snake.rainbowSnake(200), 10, 40);
        coord.pushAnimation("Rainbow Overlay", rainbow_overlay, 10, false);
        an.displayFrames(25);

        an.display.window.close();
        
        //an.display.writeToFile("CoordinatorFalseTransparency.bin");
        assertTrue(an.display.compareToFile("CoordinatorFalseTransparency.bin"));
    }
}
