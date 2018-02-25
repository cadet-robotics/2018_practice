package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.OI;
import com.hiltonrobotics.steamworksbot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class FreezeDriveCommand extends Command {
	public FreezeDriveCommand() {
		requires(DriveSubsystem.getInstance());
	}
	
	@Override
	protected void execute() {
		super.execute();
		OI.leftMotor.setSpeed(0);
		OI.rightMotor.setSpeed(0);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
}
