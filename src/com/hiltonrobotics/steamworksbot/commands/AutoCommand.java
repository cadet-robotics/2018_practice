package com.hiltonrobotics.steamworksbot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoCommand extends CommandGroup {
	public static double timestamp = 0;
	
	/*
	 * place (0 = left, 1 = center, 2 = right): placement position out of 3 alliance robots
	 * isOursRight (true = right, false = left): which side of the nearest scale we own
	 * offset (-2, -1, 0, 1, 2): the offset from the side of the scale we want (where place = 0, isOursRight = true then offset = 2)
	 */
	
	public AutoCommand(int placeIn, boolean isOursRightIn) {
		int place = placeIn;
		boolean isOursRight = isOursRightIn;
		int offset = (isOursRight ? 2 : 0) - place;
		if (offset == 0) {
			addSequential(new MoveCommand(30));								// 0-4
		} else {
			addSequential(new MoveCommand(8));								// 0
			addSequential(new TurnCommand((offset > 0) ? -90 : 90, 0.5));	// 1
			addSequential(new MoveCommand(Math.abs(offset) * 26));			// 2
			addSequential(new TurnCommand((offset > 0) ? 90 : -90));		// 3
			addSequential(new MoveCommand(22));								// 4
		}
		addSequential(new TurnCommand(isOursRight ? -90 : 90));				// 5
		addSequential(new ArmExtremeCommand(true));							// 6
		addSequential(new ShoveCommand(1.5, -0.4));							// 7
		addSequential(new ClawCommand(true));								// 8
		addSequential(new MoveCommand(4));									// 9
		addSequential(new ClawCommand(false));								// 10
		//addSequential(new ArmExtremeCommand(false));						// 11
	}

	@Override
	protected boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous() || super.isFinished();
	}
}
