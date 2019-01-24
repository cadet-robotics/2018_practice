package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ArmSubsystem extends Subsystem {
	private static ArmSubsystem instance;
	
	private ArmSubsystem() {
	}
	
	public static ArmSubsystem getInstance() {
		if (instance == null) {
			instance = new ArmSubsystem();
		}
		return instance;
	}
	
	@Override
	public void initDefaultCommand() {
	}
}