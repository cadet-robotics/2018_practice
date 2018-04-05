/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.hiltonrobotics.steamworksbot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public static final double MIN_MOTOR_SPEED = 0.4;
	public static final double MAX_MOTOR_SPEED = 0.65;
	public static final double MIN_TURN_SPEED = 0.5;
	public static final double MAX_TURN_SPEED = 0.6;
	
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	
	//This class is meant to be used for command based robots, as the description says
	//According to what we've been doing we aren't using a command based robot
	//The method I (Alex) was using before is the normal way for a timed robot, what we were doing
	//Also this entire file is meant for a command based 2017 robot - It's outdated
	public static Joystick controller = new Joystick(0);								//Primary driving controller
	public static Joystick controller2 = new Joystick(1);								//Secondary controller
	public static JoystickButton buttonA = new JoystickButton(controller, 1);			//A button input
	public static JoystickButton buttonB = new JoystickButton(controller, 2);			//B button input
	public static JoystickButton buttonX = new JoystickButton(controller, 3);			//X button input
	public static JoystickButton buttonY = new JoystickButton(controller, 4);			//Y button input
	public static JoystickButton buttonLB = new JoystickButton(controller, 5);			//LB button input
	public static JoystickButton buttonRB = new JoystickButton(controller, 6);			//RB button input
	public static JoystickButton buttonLT = new JoystickButton(controller, 99);			//LT button input
	public static JoystickButton buttonRT = new JoystickButton(controller, 99);			//RT button input
	public static DigitalInput limitLow = new DigitalInput(9);							//Lower limit switch
	public static DigitalInput limitHigh = new DigitalInput(8);							//Upper limit switch
	public static DigitalInput limitLift = new DigitalInput(7);							//Lift limit switch
	public static DigitalInput limitCube = new DigitalInput(6);							//Limit switch for the claw (if there's a cube in)
	public static Spark leftMotor = new Spark(0); 										//Left-side motor for movement
	public static Spark rightMotor = new Spark(1);										//Right-side motor for movement
	public static Spark liftMotor3 = new Spark(6);										//Motor to operate the lift (3)
	public static VictorSP clawMotorL = new VictorSP(2);								//Motor to move the claw arm (left)
	public static VictorSP clawMotorR = new VictorSP(3);								//Motor to move the claw arm (right)
	public static VictorSP liftMotor1 = new VictorSP(4);								//Motor to operate the lift (1)
	public static VictorSP liftMotor2 = new VictorSP(5);								//Motor to operate the lift (2)
	public static VictorSP cubeMotorL = new VictorSP(7);								//Motor to take in/eject cubes (left)
	public static VictorSP cubeMotorR = new VictorSP(8);								//Motor to take in/eject cubes (right)
	public static DoubleSolenoid claw = new DoubleSolenoid(0, 1); 						//Double solenoid for the claw
	public static Solenoid hookSol = new Solenoid(2);									//Lift solenoid 1
	public static Solenoid winchSol = new Solenoid(3);									//Lift solenoid 2
	public static PowerDistributionPanel pdp = new PowerDistributionPanel();			//PDP board object
	
	public static DigitalInput pos1 = new DigitalInput(4);
	public static DigitalInput pos2 = new DigitalInput(5);
	public static DigitalInput pos3 = new DigitalInput(6);
	public static AnalogInput pos4 = new AnalogInput(0);
	
	public static final double PULSE_PER_ROT = 1440;
	public static Encoder leftEncoder = new Encoder(1, 0); // Channels are reversed, see http://www.andymark.com/product-p/am-2816a.htm
	public static Encoder rightEncoder = new Encoder(3, 2); // Channels are reversed, see http://www.andymark.com/product-p/am-2816a.htm
	static {
		rightEncoder.setReverseDirection(true); // Right motor is inverted, so right encoder is inverted
		/*Stats.getInstance().add(new StatElement<Double>() {
			@Override
			public String getKey() {
				return "leftEncoder";
			}

			@Override
			public Double getValue() {
				return leftEncoder.getDistance();
			}

			@Override
			public boolean isDone() {
				return false;
			}
		});*/
		/*Stats.getInstance().add(new StatElement<Double>() {
			@Override
			public String getKey() {
				return "rightEncoder";
			}

			@Override
			public Double getValue() {
				return rightEncoder.getDistance();
			}

			@Override
			public boolean isDone() {
				return false;
			}
		});*/
	}
	
	public static Gyro gyro = new ADXRS450_Gyro();
	
	public static boolean hasGyroBeenCalibrated = false;
	public static void calibrateGyroSafe() {
		StackTraceElement e = Thread.currentThread().getStackTrace()[1];
		if (!e.getClass().getSimpleName().equals("Stats")) {
			System.out.println("Class " + e.getClassName() + " attempted to trigger callibration");
		}
		if (!hasGyroBeenCalibrated) {
			//gyro.reset();//calibrate();
		} else {
			System.out.println("Stats class attempted to retrigger callibration");
		}
	}
}