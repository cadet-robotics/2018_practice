package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.OI;
import com.hiltonrobotics.steamworksbot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class ShoveCommand extends Command {
	private double secs;
	private double speed;
	public ShoveCommand(double secsIn, double speedIn) {
		secs = secsIn;
		speed = speedIn;
		requires(DriveSubsystem.getInstance());
	}
	
	@Override
	protected void execute() {
		super.execute();
		OI.leftMotor.set(speed);
		OI.rightMotor.set(-speed);
	}

	@Override
	protected boolean isFinished() {
		return timeSinceInitialized() >= secs;
	}
}