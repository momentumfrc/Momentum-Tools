package org.usfirst.frc.team4999.lights.animations;

import java.util.ArrayList;

import org.usfirst.frc.team4999.lights.Packet;

public class Overlay implements Animation {

    private class AnimationTiming {

        public Animation animation;
        public long nextTimeMs;
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
        long now = System.currentTimeMillis();

        for(int i = 0; i < animations.length; i++) { 
            AnimationTiming curr = animations[i];
            if(curr.resetTiming) {
                curr.resetTiming = false;
                curr.nextTimeMs = now + curr.animation.getFrameDelayMilliseconds();
            }
        }

        long mindiff = animations[0].nextTimeMs - now;
        for(int i = 0; i < animations.length; i++) {
            AnimationTiming curr = animations[i];
            long currdiff = curr.nextTimeMs - now;
            if(mindiff > currdiff)
                mindiff = currdiff;
        }

        for(int i = 0; i < animations.length; i++) {
            AnimationTiming curr = animations[i];
            long diff = curr.nextTimeMs - now;
            if(diff == mindiff) {
                curr.resetTiming = true;
            }
        }

        return (int) mindiff;
    }

}
