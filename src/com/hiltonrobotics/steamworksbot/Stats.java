package com.hiltonrobotics.steamworksbot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Stats {
	public static final double WEEL_DIAMETER = 6;
	public static double ROTATION_DISTANCE_MOVED = Math.PI * WEEL_DIAMETER;
	
	private Thread updates;
	private ArrayList<StatElement<? extends Object>> elements = new ArrayList<>();
	private Stats() {
		OI.calibrateGyroSafe();
		updates = new Thread() {
			@Override
			public void run() {
				super.run();
				while (!Thread.interrupted()) {
					synchronized (updates) {
						for (StatElement<? extends Object> e : elements) {
							System.out.println("Running " + e.getKey());
							if (e.isDone()) {
								System.out.println("Removing " + e.getKey());
								elements.remove(e);
								continue;
							}
							Object o = e.getValue();
							if (o instanceof Double) {
								SmartDashboard.putNumber(e.getKey(), (Double) o);
							} else if (o instanceof String) {
								SmartDashboard.putString(e.getKey(), (String) o);
							} else {
								SmartDashboard.putString(e.getKey(), o.toString());
							}
						}
					}
					try {
						wait(0, 100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		updates.start();
	}
	
	private static Stats instance = null;
	
	public static Stats getInstance() {
		if (instance == null) {
			instance = new Stats();
		}
		return instance;
	}
	
	public void add(StatElement<? extends Object> e) {
		System.out.println("Adding " + e.getKey() + "...");
		synchronized (elements) {
			elements.add(e);
		}
		System.out.println("Added " + e.getKey());
	}
}