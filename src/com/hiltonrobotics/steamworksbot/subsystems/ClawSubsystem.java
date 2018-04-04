package com.hiltonrobotics.steamworksbot.subsystems;

import com.hiltonrobotics.steamworksbot.commands.CubeReceiveCommand;

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
		setDefaultCommand(new CubeReceiveCommand(false));
	}
	
	public static void receive() {
		(new CubeReceiveCommand(true)).start();
	}
	
	public static void close() {
		(new CubeReceiveCommand(false)).start();
	}
}
