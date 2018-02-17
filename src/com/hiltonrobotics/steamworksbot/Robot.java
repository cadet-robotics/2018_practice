/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.hiltonrobotics.steamworksbot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
/*import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.hiltonrobotics.steamworksbot.commands.ExampleCommand;
import com.hiltonrobotics.steamworksbot.subsystems.ExampleSubsystem;*/
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	//public static final ExampleSubsystem kExampleSubsystem
	//		= new ExampleSubsystem();
	public static OI m_oi;

	/*Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();*/

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public static AutoCamManager camManager = null;
	
	@Override
	public void robotInit() {
		/*m_oi = new OI();
		m_chooser.addDefault("Default Auto", new ExampleCommand());*/
		// chooser.addObject("My Auto", new MyAutoCommand());
		//SmartDashboard.putData("Auto mode", m_chooser);
		camManager = AutoCamManager.getInstance();
		//OI.gyro.calibrate();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		//Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		//m_autonomousCommand = m_chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		
		/*if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}*/
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		//Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		
		/*if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}*/
	}

	/**
	 * This function is called periodically during operator control.
	 */
	
	private int ticks = 0;
	private int state = -1;
	
	public void periodicActuator() {
	/*	if ((ticks % 100) == 0) {
			state = (state + 1) % 2;
			switch (state) {
				case 0: System.out.println("forward"); break;
				//case 1: System.out.println("off"); break;
				case 1: System.out.println("reverse"); break;
			}
		}
		switch (state) {
			case 0: OI.solenoid.set(DoubleSolenoid.Value.kForward); break;
			//case 1: OI.solenoid.set(DoubleSolenoid.Value.kOff); break;
			case 1: OI.solenoid.set(DoubleSolenoid.Value.kReverse); break;
		}
		++ticks;*/
	}
	
	public void periodicGyro() {
		//System.out.println(OI.gyro.getAngle());
	}
	
	String gameData = "";													//Game data string
	double controlX = 0;													//X-Axis of left joystick
	double controlY = 0;													//Y-Axis of left joystick
	double controlThrottle = 0;												//Throttle-Axis of the right joystick
	double moveSpeed = 1;													//Movement motor speed multiplier
	double clawSpeed = 0.6;													//Claw movement motor speed mutiplier
	double clawMoveTrimSpeed = 0.15;										//Claw movement trim speed
	double liftSpeed = 0.7;													//Multiplier for the lift speed
	double clawSafeSpeed = 0.2;												//Speed for the claw to correct at limit switches
	int dpad = 0;															//Value for the dpad 'angle'
	boolean dpadUp = false;													//D-pad up button
	boolean dpadDown = false;												//D-pad down button
	boolean dpadLeft = false;												//D-pad left button
	boolean dpadRight = false;												//D-pad right button
	boolean clawOpen = false;
	
	@Override
	public void teleopPeriodic() {
		//Scheduler.getInstance().run();
		//System.out.println(String.valueOf(OI.sensorDist.distMM()));
		//OI.servo.setAngle(OI.joystick.getY() * 10 + 90);
		//OI.servo.setAngle(OI.joystick.getY() * 4000);
		
		//periodicGyro();
		
		//OI.motor.setSpeed(0.5);
		//System.out.println(OI.panel.getTotalPower());
		
		setInputs();														//Sets input variables
		resetMotors();														//Resets motors
		setClaw();															//Sets claw/arm position
		setDriveMotors();													//Set drive motors
		setLiftMotors();													//Set lift motors
		printStatuses();													//Print device statuses such as PDP voltage
	}
	
	public void printStatuses() {
<<<<<<< HEAD
		System.out.println("PDP Voltage: " + pdp.getVoltage());
		System.out.println("Low: " + limitLow.get());
		System.out.println("Upper: " + limitHigh.get());
=======
		System.out.println("PDP Voltage: " + OI.pdp.getVoltage());
>>>>>>> 13bba30c28a9c12f62cdf420f895ff29a02df94b
	}
	
	public void setLiftMotors() {											//Set lift motors
		if(OI.buttonA.get()) {													//If the A button is pressed, switch Y-Axis to be lift motor
			OI.liftMotor1.setSpeed(controlY * liftSpeed);
			OI.liftMotor2.setSpeed(controlY * liftSpeed);
		}
	}
	
	public void setDriveMotors() {											//Set drive motors
		if(!OI.buttonA.get()) {												//Only move when A isn't pressed
			if(Math.abs(controlX) > Math.abs(controlY)) {					//Apply whichever axis is of greater absolute value
				OI.leftMotor.setSpeed(controlX * moveSpeed);					//Turn robot
				OI.rightMotor.setSpeed(controlX * moveSpeed);					//TODO: test and swap both to negative if wrong way
			} else {
				OI.leftMotor.setSpeed(controlY * moveSpeed);					//Move robot forwards/backwards
				OI.rightMotor.setSpeed(-(controlY * moveSpeed));				//TODO: test and swap negative to leftMotor if wrong way
			}
		}
	}
	
	public void setClaw() {
		if(clawOpen) {														//Set claw open/closed
			OI.claw.set(DoubleSolenoid.Value.kReverse);
		} else {
			OI.claw.set(DoubleSolenoid.Value.kForward);
		}
		
<<<<<<< HEAD
		if(controlThrottle > 0) {											//Move arm
			if(!limitLow.get()) {											//Only allow movement in the direction opposite a pressed limit switch
				clawMotorL.setSpeed(-(controlThrottle * clawSpeed));
				clawMotorR.setSpeed(controlThrottle * clawSpeed);
			}
		} else {
			if(!limitHigh.get()) {
				clawMotorL.setSpeed(-(controlThrottle * clawSpeed));
				clawMotorR.setSpeed(controlThrottle * clawSpeed);
			}
=======
		if(!clawEnd) {														//Only move with claw closed
			OI.clawMotorL.setSpeed(-(controlThrottle * clawSpeed));			//Move claw
			OI.clawMotorR.setSpeed(controlThrottle * clawSpeed);
		}																	//TODO: Use input from limit switches
		
		/*
		clawEnd = false;
		if(!limitLow.get()) {
			clawMotorL.setSpeed(clawSafeSpeed);
			clawMotorR.setSpeed(-clawSafeSpeed);
			clawEnd = true;
		} else if(!limitHigh.get()) {
			clawMotorL.setSpeed(-clawSafeSpeed);
			clawMotorR.setSpeed(clawSafeSpeed);
			clawEnd = true;
>>>>>>> 13bba30c28a9c12f62cdf420f895ff29a02df94b
		}
		
		trimClaw();
	}
	
	public void trimClaw() {												//Trim claw arm position - separate control over motors
		if(dpadUp) {
			OI.clawMotorL.setSpeed(clawMoveTrimSpeed);
		} else if(dpadDown) {
			OI.clawMotorR.setSpeed(-clawMoveTrimSpeed);
		}
		if(dpadLeft) {
			OI.clawMotorR.setSpeed(clawMoveTrimSpeed);
		} else if(dpadRight) {
			OI.clawMotorL.setSpeed(-clawMoveTrimSpeed);
		}
	}
	
	public void resetMotors() {												//Reset motors
		OI.leftMotor.setSpeed(0);
		OI.rightMotor.setSpeed(0);
		OI.clawMotorL.setSpeed(0);
		OI.clawMotorR.setSpeed(0);
	}
	
	public void setInputs() {												//Sets variables at the start of a tick
		gameData = DriverStation.getInstance().getGameSpecificMessage();	//Get game data string
		
		controlX = OI.controller.getX();										//Get X-Axis
		controlY = OI.controller.getY();										//Get Y-Axis
		controlThrottle = OI.controller.getThrottle();							//Get Throttle-Axis
		
		clawOpen = OI.buttonRT.get();											//Open claw when RT pressed
		
		dpad = OI.controller.getPOV();											//Get D-pad angle
		dpadUp = (dpad == 0 || dpad == 315 || dpad == 45);					//Set D-pad sides to separate values
		dpadDown = (dpad == 135 || dpad == 180 || dpad == 225);
		dpadLeft = (dpad == 225 || dpad == 270 || dpad == 315);
		dpadRight = (dpad == 45 || dpad == 90 || dpad == 135);
		
		if(controlThrottle == -0.0078125) controlThrottle = 0;				//Correct right joystick - rest position was showing -0.0078125
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
