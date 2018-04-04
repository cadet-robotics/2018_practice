package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.OI;
import com.hiltonrobotics.steamworksbot.subsystems.ClawSubsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

public class CubeReceiveCommand extends Command {
	private boolean isOpen;
	public CubeReceiveCommand(boolean isOpenIn) {
		isOpen = isOpenIn;
		requires(ClawSubsystem.getInstance());
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		if (isOpen) {
			OI.claw.set(DoubleSolenoid.Value.kForward);
			OI.clawReceiveMotors.set(0.5);
		} else {
			OI.claw.set(DoubleSolenoid.Value.kReverse);
			OI.clawReceiveMotors.set(0);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return isOpen && OI.receiveTrigger.get();
	}
}