package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.OI;
import com.hiltonrobotics.steamworksbot.subsystems.ClawSubsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

public class ClawCommand extends Command {
	private boolean isOpen;
	
	public ClawCommand(boolean isOpenIn) {
		isOpen = isOpenIn;
		requires(ClawSubsystem.getInstance());
	}
	
	@Override
	protected void execute() {
		super.execute();
		if (isOpen) {
			OI.claw.set(DoubleSolenoid.Value.kForward);
		} else {
			OI.claw.set(DoubleSolenoid.Value.kReverse);
		}
	}
	
	public boolean isOpening() {
		return isOpen;
	}

	@Override
	protected boolean isFinished() {
		return timeSinceInitialized() > 0.5;
	}
}