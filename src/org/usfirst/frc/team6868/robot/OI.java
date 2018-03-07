package org.usfirst.frc.team6868.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {					//Class that holds motors and controller inputs
	public static Joystick controller = new Joystick(0);								//Primary driving controller
	public static Joystick controller2 = new Joystick(1);								//Secondary controller
	public static JoystickButton buttonA = new JoystickButton(controller, 2);			//A button input
	public static JoystickButton buttonB = new JoystickButton(controller, 3);			//B button input
	public static JoystickButton buttonX = new JoystickButton(controller, 1);			//X button input
	public static JoystickButton buttonY = new JoystickButton(controller, 4);			//Y button input
	public static JoystickButton buttonLB = new JoystickButton(controller, 5);			//LB button input
	public static JoystickButton buttonRB = new JoystickButton(controller, 6);			//RB button input
	public static JoystickButton buttonLT = new JoystickButton(controller, 7);			//LT button input
	public static JoystickButton buttonRT = new JoystickButton(controller, 8);			//RT button input
	public static DigitalInput limitLow = new DigitalInput(9);							//Lower limit switch
	public static DigitalInput limitHigh = new DigitalInput(8);							//Upper limit switch
	public static DigitalInput limitLift = new DigitalInput(7);							//Lift limit switch
	public static Spark leftMotor = new Spark(0); 										//Left-side motor for movement
	public static Spark rightMotor = new Spark(1);										//Right-side motor for movement
	public static Spark liftMotor3 = new Spark(6);										//Motor to operate the lift (3)
	public static VictorSP clawMotorL = new VictorSP(2);								//Motor to move the claw arm (left)
	public static VictorSP clawMotorR = new VictorSP(3);								//Motor to move the claw arm (right)
	public static VictorSP liftMotor1 = new VictorSP(4);								//Motor to operate the lift (1)
	public static VictorSP liftMotor2 = new VictorSP(5);								//Motor to operate the lift (2)
	public static DoubleSolenoid claw = new DoubleSolenoid(0, 1); 						//Double solenoid for the claw
	public static Solenoid lift1 = new Solenoid(2);										//Lift solenoid 1
	public static Solenoid lift2 = new Solenoid(3);										//Lift solenoid 2
	public static PowerDistributionPanel pdp = new PowerDistributionPanel();			//PDP board object
}
