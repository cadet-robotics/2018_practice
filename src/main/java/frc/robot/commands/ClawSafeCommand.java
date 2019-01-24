package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class ClawSafeCommand extends Command {
	private boolean isOpen;
	
	public ClawSafeCommand(boolean isOpenIn) {
		isOpen = isOpenIn;
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		new ClawCommand(isOpen).start();
	}
	
	@Override
	protected boolean isFinished() {
		return timeSinceInitialized() > 0.5;
	}
}
