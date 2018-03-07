package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.JavaIsCancerChangeMyMind;
import com.hiltonrobotics.steamworksbot.OI;
import com.hiltonrobotics.steamworksbot.StatElement;
import com.hiltonrobotics.steamworksbot.Stats;
import com.hiltonrobotics.steamworksbot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnCommand extends PIDCommand {
	public static final double P = 0.02;
	public static final double I = 0;
	public static final double D = 0;
	
	public TurnCommand(double goalIn, double tolerance) {
		super(P, I, D);
		SmartDashboard.putNumber("turnTo", goalIn);
		requires(DriveSubsystem.getInstance());
		getPIDController().setPercentTolerance(DEFAULT_TOLERANCE);
		setInputRange(0, 360);
		getPIDController().setContinuous();
		setSetpoint(JavaIsCancerChangeMyMind.moduloIsCancer((OI.gyro.getAngle() - goalIn), 360));
		
		Stats.getInstance().add(new StatElement<Double>() {
			@Override
			public String getKey() {
				return "Goal";
			}

			@Override
			public Double getValue() {
				SmartDashboard.putNumber("turn", getSetpoint());
				return getSetpoint();
			}

			@Override
			public boolean isDone() {
				return isCompleted();
			}
		});
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
		return JavaIsCancerChangeMyMind.moduloIsCancer(OI.gyro.getAngle(), 360);
	}

	@Override
	protected void usePIDOutput(double output) {
		if (output < 0) {
			if (output > -0.2) output = -0.2;
		} else {
			if (output < 0.2) output = 0.2;
		}
		
		SmartDashboard.putNumber("TurnSpeed", output);
		
		OI.leftMotor.setSpeed(output);
		OI.rightMotor.setSpeed(output);
	}

	@Override
	protected boolean isFinished() {
		if (this.getPIDController().onTarget()) {
			System.out.println("Finished turn command");
			return true;
		} else return false;
	}
}