package org.usfirst.frc.team4999.pid;


import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;

public class MomentumPIDRunner extends Thread {
	
	private final static double DELAY = 0.05;

	ArrayList<MomentumPID> controllers;
	
	private boolean dead = false;
	
	public MomentumPIDRunner(MomentumPID[] controllers) {
		super();
		this.controllers = new ArrayList<MomentumPID>(controllers.length);
		for(MomentumPID cont : controllers)
			this.controllers.add(cont);
		setName("PID Calculator");
	}
	public MomentumPIDRunner() {
		super();
		this.controllers = new ArrayList<MomentumPID>();
		setName("PID Calculator");
	}
	
	@Override
	public void run() {
		
		while(!Thread.interrupted()) {
			
			Timer.delay(DELAY);
			
			synchronized(controllers) {
				for(MomentumPID cont : controllers) {
					if(cont.isEnabled())
						cont.calculate();
				}
			}
		}
		// Safety measure - When the controller is disabled, set its output to zero
		for(MomentumPID cont : controllers) {
			cont.zeroOutput();
		}
		dead = true;
	}
	
	public void addController(MomentumPID cont) {
		synchronized(controllers) {
			controllers.add(cont);
		}
	}
	
	public boolean isDead() {
		return dead;
	}

}
