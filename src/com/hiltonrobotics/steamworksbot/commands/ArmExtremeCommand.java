package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.OI;
import com.hiltonrobotics.steamworksbot.subsystems.ArmSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class ArmExtremeCommand extends Command {
	private boolean isTop;
	
	public ArmExtremeCommand(boolean isTopIn) {
		isTop = isTopIn;
		requires(ArmSubsystem.getInstance());
	}
	
	@Override
	protected void execute() {
		super.execute();
		System.out.println("Executing ArmExtreme");
		if ((isTop ? OI.limitHigh : OI.limitLow).get()) {
			System.out.println("Stopping");
			OI.clawMotorL.set(0);
			OI.clawMotorR.set(0);
		} else {
			System.out.println("Running");
			OI.clawMotorL.set(isTop ? -0.4 : 0.4);
			OI.clawMotorR.set(isTop ? 0.4 : -0.4);
		}
	}

	@Override
	protected boolean isFinished() {
		System.out.println("Ending");
		return (isTop ? OI.limitHigh : OI.limitLow).get();
	}
}
