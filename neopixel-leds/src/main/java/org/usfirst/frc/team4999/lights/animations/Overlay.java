package org.usfirst.frc.team4999.lights.animations;

import java.util.ArrayList;

import org.usfirst.frc.team4999.lights.Packet;

public class Overlay implements Animation {

    private static final int delay_grouping = 5;

    private class AnimationTiming {

        public Animation animation;
        public int remainingDelay;
        public boolean resetTiming;
        public Packet[] currentFrame;
        
        private AnimationTiming(Animation animation) {
            this.animation = animation;
            resetTiming = true;
        }


    }

    private AnimationTiming[] animations;
    private ArrayList<Packet> packetBuffer;

    /**
     * Sends all the packets for all the animations. Animations with lower indices have their packets sent first.
     */
    public Overlay(Animation[] animations) {
        this.animations = new AnimationTiming[animations.length];
        for(int i = 0; i < animations.length; i++) {
            this.animations[i] = new AnimationTiming(animations[i]);
        }

        packetBuffer = new ArrayList<Packet>();
    }

    @Override
    public Packet[] getNextFrame() {
        packetBuffer.clear();
        
        for(int i = 0; i < animations.length; i++) { 
            if(animations[i].resetTiming) {
                animations[i].currentFrame = animations[i].animation.getNextFrame();
            }
            for(int j = 0; j < animations[i].currentFrame.length; j++) {
                packetBuffer.add(animations[i].currentFrame[j]);
            }
        }

        Packet[] outArr = new Packet[packetBuffer.size()];
        for(int i = 0; i < packetBuffer.size(); i++) {
            outArr[i] = packetBuffer.get(i);
        }

        return outArr;
    }

    @Override
    public int getFrameDelayMilliseconds() {

        for(int i = 0; i < animations.length; i++) { 
            AnimationTiming curr = animations[i];
            if(curr.resetTiming) {
                curr.resetTiming = false;
                curr.remainingDelay = curr.animation.getFrameDelayMilliseconds();
            }
        }

        AnimationTiming min_delay = animations[0];
        for(int i = 0; i < animations.length; i++) {
            AnimationTiming curr = animations[i];
            if(min_delay.remainingDelay > curr.remainingDelay)
                min_delay = curr;
        }

        int delay = min_delay.remainingDelay;
        for(int i = 0; i < animations.length; i++) {
            AnimationTiming curr = animations[i];
            curr.remainingDelay -= delay;
            if(curr.remainingDelay < delay_grouping) {
                curr.resetTiming = true;
            }
        }

        return delay;
    }

}
