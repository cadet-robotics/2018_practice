package com.hiltonrobotics.steamworksbot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Stats {
	public static final double WEEL_DIAMETER = 6;
	public static double ROTATION_DISTANCE_MOVED = Math.PI * WEEL_DIAMETER;
	
	private Thread updates;
	private ArrayList<StatElement<Object>> elements = new ArrayList<>();
	private Stats() {
		OI.calibrateGyroSafe();
		updates = new Thread() {
			@Override
			public void run() {
				super.run();
				while (!Thread.interrupted()) {
					synchronized (updates) {
						for (StatElement<Object> e : elements) {
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
						wait(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
	
	private static Stats instance = null;
	
	public static Stats getInstance() {
		if (instance == null) {
			instance = new Stats();
		}
		return instance;
	}
	
	public void add(StatElement<Object> e) {
		synchronized (elements) {
			elements.add(e);
		}
	}
}

interface StatElement<T> {
	String getKey();
	T getValue();
	boolean isDone();
}