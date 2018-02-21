package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.OI;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDCommand;

public class TurnCommand extends PIDCommand {
	public TurnCommand(double goalIn, double tolerance) {
		super(0.2, 0.2, 0.2);
		getPIDController().setAbsoluteTolerance(tolerance);
		getPIDController().setContinuous();
		setInputRange(0, 360);
		setSetpoint((OI.gyro.getAngle() + goalIn) % 360);
	}
	
	public TurnCommand(double goalIn) {
		this(goalIn, DEFAULT_TOLERANCE);
	}

	public static final double DEFAULT_TOLERANCE = 0.01;
	
	/*
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
	*/

	@Override
	protected double returnPIDInput() {
		return OI.gyro.getAngle() % 360;
	}

	@Override
	protected void usePIDOutput(double output) {
		OI.leftMotor.setSpeed(output);
		OI.rightMotor.setSpeed(output);
	}

	@Override
	protected boolean isFinished() {
		return this.getPIDController().onTarget();
	}
}