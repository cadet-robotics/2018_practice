package com.hiltonrobotics.steamworksbot.subsystems;

import com.hiltonrobotics.steamworksbot.commands.FreezeDriveCommand;

import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveSubsystem extends Subsystem {
	private static DriveSubsystem instance;
	
	private DriveSubsystem() {
	}
	
	public static DriveSubsystem getInstance() {
		if (instance == null) {
			instance = new DriveSubsystem();
		}
		return instance;
	}
	
	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new FreezeDriveCommand());
	}
}