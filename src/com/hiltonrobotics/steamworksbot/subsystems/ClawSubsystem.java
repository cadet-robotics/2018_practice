package com.hiltonrobotics.steamworksbot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ClawSubsystem extends Subsystem {
	private static ClawSubsystem instance;
	
	private ClawSubsystem() {
	}
	
	public static ClawSubsystem getInstance() {
		if (instance == null) {
			instance = new ClawSubsystem();
		}
		return instance;
	}
	
	@Override
	protected void initDefaultCommand() {
	}
}
