package frc.robot.commands;

import frc.robot.TeleopControl;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class TeleopCommand extends Command {
	public TeleopCommand() {
		requires(DriveSubsystem.getInstance());
		requires(ArmSubsystem.getInstance());
		requires(ClawSubsystem.getInstance());
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		//TeleopControl.runInit();
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