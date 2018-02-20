package com.hiltonrobotics.steamworksbot;

public class AutoControl {
	public static double timestamp = 0;
	public static boolean isInstanceOK = false;
	
	public static void runPeriodic() {
		if (timestamp < 1.5) {
			OI.rightMotor.setSpeed(-0.4);
			OI.leftMotor.setSpeed(0.4);
		} else {
			OI.rightMotor.setSpeed(0);
			OI.leftMotor.setSpeed(0);
		}
		try {
			timestamp += (isInstanceOK) ? Robot.class.getDeclaredField("m_period").getDouble(Robot.instance) : Robot.DEFAULT_PERIOD;
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			timestamp += Robot.DEFAULT_PERIOD;
			isInstanceOK = false;
		}
	}
	
	public static void init() {
		if (Robot.instance != null) {
			try {
				Robot.class.getDeclaredField("m_period").getDouble(Robot.instance);
				isInstanceOK = true;
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
