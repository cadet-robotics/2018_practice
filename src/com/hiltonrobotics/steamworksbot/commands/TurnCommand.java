package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.OI;

import edu.wpi.first.wpilibj.command.Command;

public class TurnCommand extends Command {
	public static final double DEFAULT_ACCURACY = 0.5;
	
	private double goal = -1;
	private double accuracy = -1;
	
	public void setTurn(double deg, double accuracyIn) {
		goal = (deg + OI.gyro.getAngle()) % 360;
		accuracy = accuracyIn;
	}
	
	public void setTurn(double deg) {
		setTurn(deg, DEFAULT_ACCURACY);
	}
	
	@Override
	protected void execute() {
		if (goal == -1) {
			throw new RuntimeException("Turn degrees not set");
		}
		super.execute();
		double turnDist = goal - OI.gyro.getAngle();
		double d = Math.abs(turnDist);
		if (d < 0.5)
		if (d >= 180) {
			turnDist = (360 - turnDist) % 360;
		}
		if (turnDist > 45) turnDist = 45;
		if (turnDist < -45) turnDist = -45;
		OI.leftMotor.setSpeed(turnDist / 45 * 0.8);
		OI.rightMotor.setSpeed(-turnDist / 45 * 0.8);
	}
	
	private boolean isFinished = false;
	
	@Override
	protected boolean isFinished() {
		return isFinished;
	}
}
