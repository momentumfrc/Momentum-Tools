package org.usfirst.frc.team4999.lights;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import org.usfirst.frc.team4999.lights.animations.Animation;
import org.usfirst.frc.team4999.lights.animations.Overlay;
import org.usfirst.frc.team4999.lights.animations.Solid;

public class AnimationCoordinator {
    private static final Animation base = new Solid(Color.BLACK);

    private final class AnimationHolder {
        private String key;
        private Animation animation;
        private int priority;
        private boolean transparent;
        
        public AnimationHolder(String key, Animation animation, int priority, boolean transparent) {
            this.key = key;
            this.animation = animation;
            this.priority = priority;
            this.transparent = transparent;
        }

        public String getKey() {
            return key;
        }

        public Animation getAnimation() {
            return animation;
        }

        public int getPriority() {
            return priority;
        }

        public boolean isTransparent() {
            return transparent;
        }
    }

    private Animator animator;

    private HashMap<String, AnimationHolder> animationTable;
    private HashMap<Integer, AnimationHolder> priorityTable;
    private Vector<String> animationStack;
    private Comparator<String> animationStackSorter = (s1, s2) -> {return animationTable.get(s1).getPriority() - animationTable.get(s2).getPriority();};

    private ArrayList<Animation> visibleAnimations;

    public AnimationCoordinator(Animator animator) {
        animationTable = new HashMap<String, AnimationHolder>();
        priorityTable = new HashMap<Integer, AnimationHolder>();
        animationStack = new Vector<String>();
        visibleAnimations = new ArrayList<Animation>();

        this.animator = animator;

        updateAnimator();
    }

    public AnimationCoordinator() {
        this(null);
    }

    public void pushAnimation(String key, Animation animation, int priority, boolean transparent) {
        if(animationTable.containsKey(key)) {
            return;
        }
        if(priorityTable.containsKey(priority)) {
            popAnimation(priorityTable.get(priority).getKey());
        }

        AnimationHolder holder = new AnimationHolder(key, animation, priority, transparent);
        animationTable.put(key, holder);
        priorityTable.put(priority, holder);
        animationStack.add(key);

        Collections.sort(animationStack, animationStackSorter);

        updateAnimator();
    }

    public Animation popAnimation(String key) {
        AnimationHolder holder;

        if(!animationTable.containsKey(key))
            return null;
        
        animationStack.removeElement(key);
        holder = animationTable.remove(key);
        priorityTable.remove(holder.getPriority());

        updateAnimator();

        return holder.getAnimation();
    }

    public Animation getCurrentAnimation() {
        visibleAnimations.clear();


        for(int i = animationStack.size() - 1; i >= 0; i--) {
            AnimationHolder curr = animationTable.get(animationStack.get(i));
            visibleAnimations.add(curr.getAnimation());
            if(!curr.isTransparent()) {
                break;
            }
        }

        visibleAnimations.add(base);

        Animation[] animationsArray = new Animation[visibleAnimations.size()];
        for(int i = 0; i < animationsArray.length; i++) {
            animationsArray[i] = visibleAnimations.get(animationsArray.length - 1 - i);
        }

        return new Overlay(animationsArray);
    }

    public void updateAnimator() {
        if(animator != null) {
            animator.setAnimation(getCurrentAnimation());
        }
    }

    public boolean hasAnimation(String key) {
        return animationTable.containsKey(key);
    }

}
