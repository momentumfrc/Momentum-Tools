package org.usfirst.frc.team4999.lights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.usfirst.frc.team4999.lights.animations.Animation;
import org.usfirst.frc.team4999.lights.animations.Overlay;
import org.usfirst.frc.team4999.lights.animations.Solid;

public class AnimationCoordinator {
    private static final Animation defaultBase = new Solid(Color.WHITE);

    private class AnimationHolder {
        private String key;
        private Animation animation;
        private boolean transparent;
        
        public AnimationHolder(String key, Animation animation, boolean transparent) {
            this.key = key;
            this.animation = animation;
            this.transparent = transparent;
        }

        public String getKey() {
            return key;
        }

        public Animation getAnimation() {
            return animation;
        }

        public boolean isTransparent() {
            return transparent;
        }
    }

    private Animation base;
    private HashMap<String, AnimationHolder> animationTable;
    private Vector<String> animationStack;

    private ArrayList<Animation> visibleAnimations;

    public AnimationCoordinator() {
        base = defaultBase;
        animationTable = new HashMap<String, AnimationHolder>();
        animationStack = new Vector<String>();
    }

    public void pushAnimation(String key, Animation animation, boolean transparent) {
        if(animationTable.containsKey(key)) {
            return;
        }

        AnimationHolder holder = new AnimationHolder(key, animation, transparent);
        animationTable.put(key, holder);
        animationStack.add(key);
    }

    public Animation popAnimation(String key) {
        animationStack.removeElement(key);
        return animationTable.remove(key).animation;
    }

    public Animation getCurrentAnimation() {
        visibleAnimations.clear();

        boolean showBase = true;

        for(int i = animationStack.size() - 1; i >= 0; i--) {
            AnimationHolder curr = animationTable.get(animationStack.get(i));
            visibleAnimations.add(curr.getAnimation());
            if(!curr.isTransparent()) {
                showBase = false;
                break;
            }
        }

        if(showBase) {
            visibleAnimations.add(base);
        } else {
            visibleAnimations.add(defaultBase);
        }

        Animation[] animationsArray = new Animation[visibleAnimations.size()];
        for(int i = 0; i < animationsArray.length; i++) {
            animationsArray[i] = visibleAnimations.get(animationsArray.length - 1 - i);
        }

        return new Overlay(animationsArray);
    }
}
