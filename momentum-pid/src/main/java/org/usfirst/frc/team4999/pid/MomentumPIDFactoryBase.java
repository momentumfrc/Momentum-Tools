package org.usfirst.frc.team4999.pid;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;


public abstract class MomentumPIDFactoryBase {
	
	protected static final String LOCATION = "/home/lvuser/";
	
	protected static final String DEFAULT_P = "0";
	protected static final String DEFAULT_I = "0";
	protected static final String DEFAULT_D = "0";
	protected static final String DEFAULT_F = "0";
	protected static final String DEFAULT_ERR_ZONE = "10";
	protected static final String DEFAULT_TARGET_ZONE = "3";
	protected static final String DEFAULT_TARGET_TIME = "0.5";
	
	protected static final MomentumPIDRunner calculator = new MomentumPIDRunner();
	
	public static void addToCalculator(MomentumPID cont) {
		calculator.addController(cont);
		if(!calculator.isAlive() && !calculator.isDead())
			calculator.start();
	}
	
	private static Properties openFile(String file) {
		System.out.println("Loading " + file);
		Properties ret = new Properties();
		try (FileInputStream input = new FileInputStream(file)) {
			ret.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return ret;
	}
	
	private static void saveFile(Properties props, String file) {
		System.out.println("Saving " + file);
		try (FileOutputStream output = new FileOutputStream(file)) {
			props.store(output, "PID Values for a PID controller");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private static void savePID(MomentumPID pid, String path) {
		Properties props = new Properties();
		props.put("P", String.format("%f", pid.getP()));
		props.put("I", String.format("%f", pid.getI()));
		props.put("D", String.format("%f", pid.getD()));
		props.put("F", String.format("%f", pid.getF()));
		props.put("Error Zone", String.format("%f", pid.getErrZone()));
		props.put("Target Zone", String.format("%f", pid.getTargetZone()));
		props.put("Target Time", String.format("%f", pid.getTargetTime()));
		saveFile(props, path);
	}

	private static void saveCANPID(SendableCANPIDController pid, String path) {
		Properties props = new Properties();
		props.put("Target Zone", String.format("%f", pid.getTargetZone()));
		props.put("Target Time", String.format("%f", pid.getTargetTime()));
	}
	
	private static boolean checkFile(String file) {
		File f = new File(file);
		return f.exists() && !f.isDirectory();
	}
	
	protected static MomentumPID loadPID(String name, String path, PIDSource source, PIDOutput output) {
		MomentumPID ret;
		if(checkFile(path)) {
			Properties props = openFile(path);
			double p = Double.parseDouble(props.getProperty("P", DEFAULT_P));
			double i = Double.parseDouble(props.getProperty("I", DEFAULT_I));
			double d = Double.parseDouble(props.getProperty("D", DEFAULT_D));
			double f = Double.parseDouble(props.getProperty("F", DEFAULT_F));
			double errZone = Double.parseDouble(props.getProperty("Error Zone", DEFAULT_ERR_ZONE));
			double targetZone = Double.parseDouble(props.getProperty("Target Zone", DEFAULT_TARGET_ZONE));
			double targetTime = Double.parseDouble(props.getProperty("Target Time", DEFAULT_TARGET_TIME));
			ret = new MomentumPID(name,p,i,d,f,errZone,targetZone,targetTime,source,output);
		} else {
			double p = Double.parseDouble(DEFAULT_P);
			double i = Double.parseDouble(DEFAULT_I);
			double d = Double.parseDouble(DEFAULT_D);
			double f = Double.parseDouble(DEFAULT_F);
			double errZone = Double.parseDouble(DEFAULT_ERR_ZONE);
			double targetZone = Double.parseDouble(DEFAULT_TARGET_ZONE);
			double targetTime = Double.parseDouble(DEFAULT_TARGET_TIME);
			ret = new MomentumPID(name,p,i,d,f,errZone,targetZone,targetTime,source,output);
			ret.setTargetTime(targetTime);
		}
		ret.setListener(()->savePID(ret, path));
		addToCalculator(ret);
		return ret;
	}

	protected static SendableCANPIDController loadCANPID(String name, String path, CANSparkMax max) {
		SendableCANPIDController ret;
		if(checkFile(path)) {
			Properties props = openFile(path);
			double targetZone = Double.parseDouble(props.getProperty("Target Zone", DEFAULT_TARGET_ZONE));
			double targetTime = Double.parseDouble(props.getProperty("Target Time", DEFAULT_TARGET_TIME));
			ret = new SendableCANPIDController(name, max, targetZone, targetTime);
		} else {
			double targetZone = Double.parseDouble(DEFAULT_TARGET_ZONE);
			double targetTime = Double.parseDouble(DEFAULT_TARGET_TIME);
			ret = new SendableCANPIDController(name, max, targetZone, targetTime);
		}
		ret.setListener(()->saveCANPID(ret, path));
		return ret;
	}
	

}
