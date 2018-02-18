package com.hiltonrobotics.steamworksbot;

public class AutoControl {
	public static double timestamp = 0;
	
	public static void runPeriodic() {
		if (timestamp < 0.5) {
			OI.rightMotor.setSpeed(0.5);
			OI.leftMotor.setSpeed(0.5);
		}
		timestamp += (Robot.instance == null) ? Robot.DEFAULT_PERIOD : Robot.instance.getPeriod();
	}
	
	public static void init() {
	}
}