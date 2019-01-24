package frc.robot.commands;

import frc.robot.OI;
import frc.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class FreezeDriveCommand extends Command {
	public FreezeDriveCommand() {
		requires(DriveSubsystem.getInstance());
	}
	
	@Override
	protected void execute() {
		super.execute();
		OI.leftMotor.setSpeed(0);
		OI.rightMotor.setSpeed(0);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
}
