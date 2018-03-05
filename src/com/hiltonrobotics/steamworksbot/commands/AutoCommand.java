package com.hiltonrobotics.steamworksbot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class AutoCommand extends Command {
	public static double timestamp = 0;
	
	/*
	 * place (0 = left, 1 = center, 2 = right): placement position out of 3 alliance robots
	 * isOursRight (true = right, false = left): which side of the nearest scale we own
	 */
	private int place;
	private boolean isOursRight;
	
	public AutoCommand(int placeIn, boolean isOursRightIn) {
		place = placeIn;
		isOursRight = isOursRightIn;
	}
	
	private Command subcommand = null;
	private int state = -1;
	public void execute() {
		if (subcommand != null) {
			if (subcommand.isRunning()) {
				return;
			} else {
				System.out.println("Finished " + subcommand.getName() + " in ~" + subcommand.timeSinceInitialized() + " seconds");
				subcommand = null;
			}
		}
		++state;
		int offset = (isOursRight ? 2 : 0) - place;
		switch (state) {
			case 0:
				if (offset == 0) {
					subcommand = new MoveCommand(24);
					state = 4;
				} else {
					subcommand = new MoveCommand(12);
				}
				break;
			case 1:
				subcommand = new TurnCommand((offset > 0) ? -90 : 90);
				break;
			case 2:
				subcommand = new MoveCommand(Math.abs(offset) * 36);
				break;
			case 3:
				subcommand = new TurnCommand((offset > 0) ? 90 : -90);
				break;
			case 4:
				subcommand = new MoveCommand(12);
				break;
			case 5:
				subcommand = new TurnCommand(isOursRight ? 90 : -90);
				break;
			default:
				this.cancel();
				System.out.println("Ending autonomous...");
				return;
		}
		subcommand.start();
		
		/*
		if (timestamp < 0.5) {
			OI.rightMotor.setSpeed(0.5);
			OI.leftMotor.setSpeed(0.5);
		}
		timestamp += (Robot.instance == null) ? Robot.DEFAULT_PERIOD : Robot.instance.getPeriod();
		*/
	}
	
	public void initialize() {
		super.initialize();
	}

	@Override
	protected boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous();
	}
	
	/*
	public static double timestamp = 0;
	public static boolean isInstanceOK = false;
	
	public static void runPeriodic() {
		if (timestamp < 1.5) {
			OI.rightMotor.setSpeed(-0.4);
			OI.leftMotor.setSpeed(0.4);
		} else {
			OI.rightMotor.setSpeed(0);
			OI.leftMotor.setSpeed(0);
		}
		try {
			timestamp += (isInstanceOK) ? Robot.class.getDeclaredField("m_period").getDouble(Robot.instance) : Robot.DEFAULT_PERIOD;
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			timestamp += Robot.DEFAULT_PERIOD;
			isInstanceOK = false;
		}
	}
	
	public static void init() {
		if (Robot.instance != null) {
			try {
				Robot.class.getDeclaredField("m_period").getDouble(Robot.instance);
				isInstanceOK = true;
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	*/
}