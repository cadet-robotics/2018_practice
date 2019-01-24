package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Stats {
	public static final double WEEL_DIAMETER = 5.5;
	public static double ROTATION_DISTANCE_MOVED = Math.PI * WEEL_DIAMETER;
	
	private Thread updates;
	private ArrayList<StatElement<? extends Object>> elements = new ArrayList<>();
	private Stats() {
		//OI.calibrateGyroSafe();
		updates = new Thread() {
			@Override
			public void run() {
				super.run();
				while (!Thread.interrupted()) {
					synchronized (elements) {
						for (int i = 0; i < elements.size();) {
							StatElement<? extends Object> e = elements.get(i);
							//System.out.println("Running " + e.getKey());
							if (e.isDone()) {
								System.out.println("Removing " + e.getKey());
								elements.remove(e);
								continue; // Not incrementing i is intentional
							}
							Object o = e.getValue();
							if (o instanceof Double) {
								SmartDashboard.putNumber(e.getKey(), (Double) o);
							} else if (o instanceof String) {
								SmartDashboard.putString(e.getKey(), (String) o);
							} else {
								SmartDashboard.putString(e.getKey(), o.toString());
							}
							i++;
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
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
		StackTraceElement trace = Thread.currentThread().getStackTrace()[1];
		System.out.println(trace.getFileName() + ": " + trace.getLineNumber());
		System.out.println("Adding " + e.getKey() + "...");
		synchronized (elements) {
			elements.add(e);
		}
		System.out.println("Added " + e.getKey());
	}
}