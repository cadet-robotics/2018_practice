package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.TeleopControl;
import com.hiltonrobotics.steamworksbot.subsystems.ArmSubsystem;
import com.hiltonrobotics.steamworksbot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class TeleopCommand extends Command {
	public TeleopCommand() {
		requires(DriveSubsystem.getInstance());
		requires(ArmSubsystem.getInstance());
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		TeleopControl.runInit();
	}
	
	@Override
	protected void execute() {
		super.execute();
		TeleopControl.runPeriodic();
	}
	
	@Override
	protected boolean isFinished() {
		return !DriverStation.getInstance().isOperatorControl();
	}
}