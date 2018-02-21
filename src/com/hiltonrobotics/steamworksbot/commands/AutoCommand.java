package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.OI;
import com.hiltonrobotics.steamworksbot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class AutoCommand extends Command {
	public static double timestamp = 0;
	
	private Command subcommand = null;
	private int state = 0;
	public void execute() {
		if (subcommand != null) {
			if (subcommand.isRunning()) {
				return;
			} else {
				System.out.println("Finished " + subcommand.getName() + " in ~" + subcommand.timeSinceInitialized() + " seconds");
				subcommand = null;
				++state;
			}
		}
		switch (state) {
			case 0:
				subcommand = new TurnCommand(90);
				Scheduler.getInstance().add(subcommand); break;
			default:
				return;
		}
		
		/*
		if (timestamp < 0.5) {
			OI.rightMotor.setSpeed(0.5);
			OI.leftMotor.setSpeed(0.5);
		}
		timestamp += (Robot.instance == null) ? Robot.DEFAULT_PERIOD : Robot.instance.getPeriod();
		*/
	}
	
	public void initialize() {
		super.initialize();
		OI.calibrateGyroSafe();
	}

	@Override
	protected boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous();
	}
	
	/*
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
				e.printStackTrace();
			}
		}
	}
	*/
}