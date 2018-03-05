package com.hiltonrobotics.steamworksbot.commands;

import com.hiltonrobotics.steamworksbot.OI;
import com.hiltonrobotics.steamworksbot.StatElement;
import com.hiltonrobotics.steamworksbot.Stats;
import com.hiltonrobotics.steamworksbot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MoveCommand extends Command {
	public static final double DEFAULT_TOLERANCE_POS = 0.005;
	
	private Double posChange = (double) 0, rotChange = (double) 0;
	
	private PIDController pidPos = new PIDController(2, 2, 2, new PIDSource() {
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
		}
		
		@Override
		public double pidGet() {
			return (OI.leftEncoder.getDistance() + OI.rightEncoder.getDistance()) / 2;
		}
		
		@Override
		public PIDSourceType getPIDSourceType() {
			return null;
		}
	}, new PIDOutput() {
		@Override
		public void pidWrite(double output) {
			synchronized (posChange) {
				posChange = Math.max(Math.min(output, 0.6), -0.6);
			}
		}
	});
	
	private PIDController pidRot = new PIDController(TurnCommand.P, TurnCommand.I, TurnCommand.D, new PIDSource() {
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
		}
		
		@Override
		public double pidGet() {
			double d = OI.gyro.getAngle() % 360;
			SmartDashboard.putNumber("rot", d);
			return d;
		}
		
		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}
	}, new PIDOutput() {
		@Override
		public void pidWrite(double output) {
			synchronized (rotChange) {
				rotChange = output;
			}
		}
	});
	
	public MoveCommand(double dist) {
		requires(DriveSubsystem.getInstance());
		OI.leftEncoder.reset();
		OI.rightEncoder.reset();
		pidPos.setSetpoint(dist);
		pidPos.setInputRange(dist * (-2), dist * 2);
		pidPos.setAbsoluteTolerance(DEFAULT_TOLERANCE_POS * 4);
		pidPos.enable();
		
		pidRot.setInputRange(0, 360);
		pidRot.setContinuous();
		double d = OI.gyro.getAngle() % 360;
		Stats.getInstance().add(new StatElement<Double>() {
			@Override
			public String getKey() {
				return null;
			}

			@Override
			public Double getValue() {
				return d;
			}

			@Override
			public boolean isDone() {
				return isCompleted();
			}
		});
		pidRot.setSetpoint(d);
		pidRot.setAbsoluteTolerance(TurnCommand.DEFAULT_TOLERANCE);
		pidRot.enable();
	}
	
	@Override
	public void execute() {
		synchronized (posChange) {
			synchronized (rotChange) {
				System.out.println(posChange + ", " + rotChange);
				OI.leftMotor.set(Math.max(Math.min(rotChange + posChange, -1), 1) / 1.5);
				OI.rightMotor.set(Math.max(Math.min(rotChange - posChange, -1), 1) / 1.5);
			}
		}
	}

	@Override
	protected boolean isFinished() {
		return pidPos.onTarget() && pidRot.onTarget();
	}
}