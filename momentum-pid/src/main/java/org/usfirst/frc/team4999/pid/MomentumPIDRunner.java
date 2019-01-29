package org.usfirst.frc.team4999.pid;


import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;

public class MomentumPIDRunner extends Thread {
	
	private final static double DELAY = 0.01; // 10 ms

	ArrayList<MotionController> controllers;
	
	private boolean dead = false;
	
	public MomentumPIDRunner(MotionController[] controllers) {
		super();
		this.controllers = new ArrayList<MotionController>(controllers.length);
		for(MotionController cont : controllers)
			this.controllers.add(cont);
		setName("PID Calculator");
	}
	public MomentumPIDRunner() {
		super();
		this.controllers = new ArrayList<MotionController>();
		setName("PID Calculator");
	}
	
	@Override
	public void run() {
		
		while(!Thread.interrupted()) {
			
			Timer.delay(DELAY);
			
			synchronized(controllers) {
				for(MotionController cont : controllers) {
					if(cont.isEnabled())
						cont.calculate();
				}
			}
		}
		// Safety measure - When the controller is disabled, set its output to zero
		for(MotionController cont : controllers) {
			cont.zeroOutput();
		}
		dead = true;
	}
	
	public void addController(MotionController cont) {
		synchronized(controllers) {
			controllers.add(cont);
		}
	}
	
	public boolean isDead() {
		return dead;
	}

}
