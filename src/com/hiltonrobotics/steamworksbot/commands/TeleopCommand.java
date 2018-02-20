package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.TeleopControl;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class TeleopCommand extends Command {
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
