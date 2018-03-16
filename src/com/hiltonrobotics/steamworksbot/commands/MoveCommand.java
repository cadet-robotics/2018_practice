package com.hiltonrobotics.steamworksbot.commands;

import java.lang.management.ClassLoadingMXBean;

import com.hiltonrobotics.steamworksbot.JavaIsCancerChangeMyMind;
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
	public static final double DEFAULT_TOLERANCE_POS = 1;
	
	private Double posChange = (double) 0, rotChange = (double) 0;
	
	private PIDController pidPos = new PIDController(0.02, 0, 0, new PIDSource() {
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
		}
		
		@Override
		public double pidGet() {
			//System.out.println("Getting distance");
			double d = (OI.leftEncoder.getDistance() + OI.rightEncoder.getDistance()) / 2 /*/ OI.PULSE_PER_ROT*/ * Stats.ROTATION_DISTANCE_MOVED;
			//System.out.println("posErr: " + d);
			return d;
		}
		
		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}
	}, new PIDOutput() {
		@Override
		public void pidWrite(double output) {
			//System.out.println("preOut: " + output);
			/*if (output < 0) {
				if (output > -OI.MIN_MOTOR_SPEED) output = -OI.MIN_MOTOR_SPEED;
			} else {
				if (output < OI.MIN_MOTOR_SPEED) output = OI.MIN_MOTOR_SPEED;
			}*/
			output = clampAbs(output, OI.MIN_MOTOR_SPEED, Double.POSITIVE_INFINITY);
			synchronized (posChange) {
				posChange = output * 1.5;//Math.max(Math.min(output, 0.6), -0.6);
				//System.out.println("out: " + posChange);
			}
		}
	});
	
	private PIDController pidRot = new PIDController(TurnCommand.P, TurnCommand.I, TurnCommand.D, new PIDSource() {
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
		}
		
		@Override
		public double pidGet() {
			double d = JavaIsCancerChangeMyMind.moduloIsCancer(OI.gyro.getAngle(), 360);
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
			/*if (output < 0) {
				if (output > -(OI.MIN_MOTOR_SPEED / 2)) output = -(OI.MIN_MOTOR_SPEED / 2);
			} else {
				if (output < (OI.MIN_MOTOR_SPEED / 2)) output = (OI.MIN_MOTOR_SPEED / 2);
			}*/
			synchronized (rotChange) {
				rotChange = output;
			}
		}
	});
	
	/**
	 * @param dist The distance to move in inches
	 */
	public MoveCommand(double dist) { // Moves the robot forward/backward
		requires(DriveSubsystem.getInstance());
		OI.leftEncoder.reset();
		OI.rightEncoder.reset();
		OI.leftEncoder.setDistancePerPulse(OI.PULSE_PER_ROT);
		OI.rightEncoder.setDistancePerPulse(OI.PULSE_PER_ROT);
		pidPos.setSetpoint(dist);
		//pidPos.setInputRange(-Math.abs(dist * 2), Math.abs(dist * 2));
		pidPos.setAbsoluteTolerance(DEFAULT_TOLERANCE_POS);
		pidPos.enable();
		
		pidRot.setInputRange(0, 360);
		pidRot.setContinuous();
		double d = JavaIsCancerChangeMyMind.moduloIsCancer(OI.gyro.getAngle(), 360);
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
				//System.out.println(posChange + ", " + rotChange);
				double l = clampAbs(rotChange + posChange, 0, OI.MAX_MOTOR_SPEED);
				double r = clampAbs(rotChange - posChange, 0, OI.MAX_MOTOR_SPEED);
				//System.out.println("left: " + l);
				//System.out.println("right: " + r);
				OI.leftMotor.set(l);
				OI.rightMotor.set(r);
			}
		}
	}
	
	public static double clamp(double n, double low, double high) {
		if (low > high) {
			double d = low;
			low = high;
			high = d;
		}
		return Math.min(Math.max(n, low), high);
	}
	
	public static double clampAbs(double n, double low, double high) {
		if (n < 0) {
			return -clamp(-n, high, low);
		} else {
			return clamp(n, low, high);
		}
	}

	@Override
	protected boolean isFinished() {
		return pidPos.onTarget();// && pidRot.onTarget();
	}
}