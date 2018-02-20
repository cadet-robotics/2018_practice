package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.OI;
import com.hiltonrobotics.steamworksbot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class AutoCommand extends Command {
	public static double timestamp = 0;
	
	public void execute() {
		if (timestamp < 0.5) {
			OI.rightMotor.setSpeed(0.5);
			OI.leftMotor.setSpeed(0.5);
		}
		timestamp += (Robot.instance == null) ? Robot.DEFAULT_PERIOD : Robot.instance.getPeriod();
	}
	
	public void initialize() {
		super.initialize();
		OI.calibrateGyroSafe();
	}

	@Override
	protected boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous();
	}
}