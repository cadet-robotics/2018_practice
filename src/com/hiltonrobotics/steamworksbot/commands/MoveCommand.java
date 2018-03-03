package com.hiltonrobotics.steamworksbot.commands;

import java.io.PipedOutputStream;

import com.hiltonrobotics.steamworksbot.OI;
import com.hiltonrobotics.steamworksbot.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

public class MoveCommand extends Command {
	public static final double DEFAULT_TOLERANCE_POS = 0.005;
	
	private Object posLock = new Object();
	private Object rotLock = new Object();
	
	private Object notifyPos = new Object();
	private Object notifyRot = new Object();
	
	private double posChange = 0, rotChange = 0;
	
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
			synchronized (posLock) {
				try {
					posLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			synchronized (posLock) {
				posChange = Math.max(Math.min(output, 0.6), -0.6);
			}
			notifyPos.notifyAll();
		}
	});
	
	private PIDController pidRot = new PIDController(0.2, 0.2, 0.2, new PIDSource() {
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
		}
		
		@Override
		public double pidGet() {
			return OI.gyro.getAngle() % 360;
		}
		
		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}
	}, new PIDOutput() {
		@Override
		public void pidWrite(double output) {
			synchronized (rotLock) {
				try {
					rotLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			synchronized (rotLock) {
				rotChange = output;
			}
			notifyRot.notifyAll();
		}
	});
	
	public static double ROTATION_DISTANCE_MOVED = Math.PI * 6;
	
	public MoveCommand(double dist) {
		requires(DriveSubsystem.getInstance());
		OI.leftEncoder.reset();
		OI.rightEncoder.reset();
		pidPos.setSetpoint(dist);
		pidPos.setInputRange(Double.MIN_VALUE, Double.MAX_VALUE);
		pidPos.setAbsoluteTolerance(DEFAULT_TOLERANCE_POS);
		
		pidRot.setInputRange(0, 360);
		pidRot.setContinuous();
		pidRot.setSetpoint(OI.gyro.getAngle() % 360);
		pidRot.setAbsoluteTolerance(TurnCommand.DEFAULT_TOLERANCE);
	}
	
	@Override
	public void execute() {
		try {
			posLock.notifyAll();
			synchronized (notifyPos) {
				notifyPos.wait(10);
			}
			rotLock.notifyAll();
			synchronized (notifyRot) {
				notifyRot.wait(10);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Command c = this;
			while (c.getGroup() != null) {
				c = c.getGroup();
			}
			c.cancel();
		}
		OI.leftMotor.set(Math.max(Math.min(rotChange + posChange, -1), 1));
		OI.rightMotor.set(Math.max(Math.min(rotChange - posChange, -1), 1));
	}

	@Override
	protected boolean isFinished() {
		return pidPos.onTarget() && pidRot.onTarget();
	}
}