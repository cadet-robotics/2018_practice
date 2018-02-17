/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.hiltonrobotics.steamworksbot;

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
	
	Joystick controller = new Joystick(0);
	JoystickButton buttonA = new JoystickButton(controller, 2);				//A button input
	JoystickButton buttonB = new JoystickButton(controller, 3);				//B button input
	JoystickButton buttonX = new JoystickButton(controller, 1);				//X button input
	JoystickButton buttonY = new JoystickButton(controller, 4);				//Y button input
	JoystickButton buttonLB = new JoystickButton(controller, 5);			//LB button input
	JoystickButton buttonRB = new JoystickButton(controller, 6);			//RB button input
	JoystickButton buttonLT = new JoystickButton(controller, 7);			//LT button input
	JoystickButton buttonRT = new JoystickButton(controller, 8);			//RT button input
	Spark leftMotor = new Spark(0); 										//Left-side motor for movement
	Spark rightMotor = new Spark(1);										//Right-side motor for movement
	VictorSP clawMotorL = new VictorSP(2);									//Motor to move the claw arm (left)
	VictorSP clawMotorR = new VictorSP(3);									//Motor to move the claw arm (right)
	VictorSP liftMotor1 = new VictorSP(4);									//Motor to operate the lift (1)
	VictorSP liftMotor2 = new VictorSP(5);									//Motor to operate the lift (2)
	DoubleSolenoid claw = new DoubleSolenoid(0, 1); 						//Double solenoid for the claw
	DoubleSolenoid lift = new DoubleSolenoid(2, 3);							//Double solenoid for the lift
	PowerDistributionPanel pdp = new PowerDistributionPanel();				//PDP board object
	String gameData = "";													//Game data string
	double controlX = 0;													//X-Axis of left joystick
	double controlY = 0;													//Y-Axis of left joystick
	double controlThrottle = 0;												//Throttle-Axis of the right joystick
	double moveSpeed = 1;													//Movement motor speed multiplier
	double clawSpeed = 0.6;													//Claw movement motor speed mutiplier
	double clawMoveTrimSpeed = 0.15;										//Claw movement trim speed
	double liftSpeed = 0.7;													//Multiplier for the lift speed
	int dpad = 0;															//Value for the dpad 'angle'
	boolean dpadUp = false;													//D-pad up button
	boolean dpadDown = false;												//D-pad down button
	boolean dpadLeft = false;												//D-pad left button
	boolean dpadRight = false;												//D-pad right button
	boolean clawOpen = false;
	boolean clawEnd = false;												//If the claw is at a limit, true
	
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
		System.out.println("PDP Voltage: " + pdp.getVoltage());
	}
	
	public void setLiftMotors() {											//Set lift motors
		if(buttonA.get()) {													//If the A button is pressed, switch Y-Axis to be lift motor
			liftMotor1.setSpeed(controlY * liftSpeed);
			liftMotor2.setSpeed(controlY * liftSpeed);
		}
	}
	
	public void setDriveMotors() {											//Set drive motors
		if(!buttonA.get()) {												//Only move when A isn't pressed
			if(Math.abs(controlX) > Math.abs(controlY)) {					//Apply whichever axis is of greater absolute value
				leftMotor.setSpeed(controlX * moveSpeed);					//Turn robot
				rightMotor.setSpeed(controlX * moveSpeed);					//TODO: test and swap both to negative if wrong way
			} else {
				leftMotor.setSpeed(controlY * moveSpeed);					//Move robot forwards/backwards
				rightMotor.setSpeed(-(controlY * moveSpeed));				//TODO: test and swap negative to leftMotor if wrong way
			}
		}
	}
	
	public void setClaw() {
		if(clawOpen) {														//Set claw open/closed
			claw.set(DoubleSolenoid.Value.kReverse);
		} else {
			claw.set(DoubleSolenoid.Value.kForward);
		}
		
		if(!clawEnd) {														//Only move with claw closed
			clawMotorL.setSpeed(-(controlThrottle * clawSpeed));			//Move claw
			clawMotorR.setSpeed(controlThrottle * clawSpeed);
		}																	//TODO: Use input from limit switches
		
		trimClaw();
	}
	
	public void trimClaw() {												//Trim claw arm position - separate control over motors
		if(dpadUp) {
			clawMotorL.setSpeed(clawMoveTrimSpeed);
		} else if(dpadDown) {
			clawMotorR.setSpeed(-clawMoveTrimSpeed);
		}
		if(dpadLeft) {
			clawMotorR.setSpeed(clawMoveTrimSpeed);
		} else if(dpadRight) {
			clawMotorL.setSpeed(-clawMoveTrimSpeed);
		}
	}
	
	public void resetMotors() {												//Reset motors
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
		clawMotorL.setSpeed(0);
		clawMotorR.setSpeed(0);
	}
	
	public void setInputs() {												//Sets variables at the start of a tick
		gameData = DriverStation.getInstance().getGameSpecificMessage();	//Get game data string
		
		controlX = controller.getX();										//Get X-Axis
		controlY = controller.getY();										//Get Y-Axis
		controlThrottle = controller.getThrottle();							//Get Throttle-Axis
		
		clawOpen = buttonRT.get();											//Open claw when RT pressed
		
		dpad = controller.getPOV();											//Get D-pad angle
		dpadUp = (dpad == 0 || dpad == 315 || dpad == 45);					//Set D-pad sides to separate values
		dpadDown = (dpad == 135 || dpad == 180 || dpad == 225);
		dpadLeft = (dpad == 225 || dpad == 270 || dpad == 315);
		dpadRight = (dpad == 45 || dpad == 90 || dpad == 135);
		//clawEnd = 														//TODO: Get input for limit switches
		
		if(controlThrottle == -0.0078125) controlThrottle = 0;				//Correct right joystick - rest position was showing -0.0078125
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
