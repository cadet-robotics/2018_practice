package com.hiltonrobotics.steamworksbot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

public class TeleopControl {
	
	static String gameData = "";													//Game data string
	static double controlX = 0;													//X-Axis of left joystick
	static double controlY = 0;													//Y-Axis of left joystick
	static double controlThrottle = 0;												//Throttle-Axis of the right joystick
	static double moveSpeed = 1;													//Movement motor speed multiplier
	static double clawSpeed = 0.6;													//Claw movement motor speed mutiplier
	static double clawMoveTrimSpeed = 0.15;										//Claw movement trim speed
	static double liftSpeed = 0.7;													//Multiplier for the lift speed
	static double clawSafeSpeed = 0.2;												//Speed for the claw to correct at limit switches
	static int dpad = 0;															//Value for the dpad 'angle'
	static boolean dpadUp = false;													//D-pad up button
	static boolean dpadDown = false;												//D-pad down button
	static boolean dpadLeft = false;												//D-pad left button
	static boolean dpadRight = false;												//D-pad right button
	static boolean clawOpen = false;
	
	public static void runPeriodic() {
		setInputs();														//Sets input variables
		resetMotors();														//Resets motors
		setClaw();															//Sets claw/arm position
		setDriveMotors();													//Set drive motors
		setLiftMotors();													//Set lift motors
		printStatuses();													//Print device statuses such as PDP voltage
	}
	
	public static void printStatuses() {
		System.out.println("PDP Voltage: " + OI.pdp.getVoltage());
	}
	
	public static void setLiftMotors() {											//Set lift motors
		if(OI.buttonA.get()) {												//If the A button is pressed, switch Y-Axis to be lift motor
			OI.liftMotor1.setSpeed(controlY * liftSpeed);
			OI.liftMotor2.setSpeed(controlY * liftSpeed);
		}
	}
	
	public static void setDriveMotors() {											//Set drive motors
		if(!OI.buttonA.get()) {												//Only move when A isn't pressed
			if(Math.abs(controlX) > Math.abs(controlY)) {					//Apply whichever axis is of greater absolute value
				OI.leftMotor.setSpeed(controlX * moveSpeed);				//Turn robot
				OI.rightMotor.setSpeed(controlX * moveSpeed);				//TODO: test and swap both to negative if wrong way
			} else {
				OI.leftMotor.setSpeed(controlY * moveSpeed);				//Move robot forwards/backwards
				OI.rightMotor.setSpeed(-(controlY * moveSpeed));			//TODO: test and swap negative to leftMotor if wrong way
			}
		}
	}
	
	public static void setClaw() {
		if(clawOpen) {														//Set claw open/closed
			OI.claw.set(DoubleSolenoid.Value.kReverse);
		} else {
			OI.claw.set(DoubleSolenoid.Value.kForward);
		}
		
		if(controlThrottle > 0) {											//Move arm
			if(!OI.limitLow.get()) {										//Only allow movement in the direction opposite a pressed limit switch
				OI.clawMotorL.setSpeed(-(controlThrottle * clawSpeed));
				OI.clawMotorR.setSpeed(controlThrottle * clawSpeed);
			}
		} else {
			if(!OI.limitHigh.get()) {
				OI.clawMotorL.setSpeed(-(controlThrottle * clawSpeed));
				OI.clawMotorR.setSpeed(controlThrottle * clawSpeed);
			}
		}
		
		trimClaw();
	}
	
	public static void trimClaw() {												//Trim claw arm position - separate control over arm motors
		if(dpadUp) {
			OI.clawMotorL.setSpeed(clawMoveTrimSpeed);
		} else if(dpadDown) {
			OI.clawMotorR.setSpeed(clawMoveTrimSpeed);
		}
		if(dpadLeft) {
			OI.clawMotorR.setSpeed(-clawMoveTrimSpeed);
		} else if(dpadRight) {
			OI.clawMotorL.setSpeed(-clawMoveTrimSpeed);
		}
	}
	
	public static void resetMotors() {												//Reset motors and solenoids
		OI.leftMotor.setSpeed(0);
		OI.rightMotor.setSpeed(0);
		OI.clawMotorL.setSpeed(0);
		OI.clawMotorR.setSpeed(0);
		OI.liftMotor1.setSpeed(0);
		OI.liftMotor2.setSpeed(0);
		OI.lift.set(DoubleSolenoid.Value.kOff);
		OI.claw.set(DoubleSolenoid.Value.kOff);
	}
	
	public static void setInputs() {												//Sets variables at the start of a tick
		gameData = DriverStation.getInstance().getGameSpecificMessage();	//Get game data string
		
		controlX = OI.controller.getX();									//Get X-Axis
		controlY = OI.controller.getY();									//Get Y-Axis
		controlThrottle = OI.controller.getThrottle();						//Get Throttle-Axis
		
		clawOpen = OI.buttonRT.get();										//Open claw when RT pressed
		
		dpad = OI.controller.getPOV();										//Get D-pad angle
		dpadUp = (dpad == 0 || dpad == 315 || dpad == 45);					//Set D-pad sides to separate values
		dpadDown = (dpad == 135 || dpad == 180 || dpad == 225);
		dpadLeft = (dpad == 225 || dpad == 270 || dpad == 315);
		dpadRight = (dpad == 45 || dpad == 90 || dpad == 135);
		
		if(controlThrottle == -0.0078125) controlThrottle = 0;				//Correct right joystick - rest position was showing -0.0078125
	}
	
	public static void runInit() {
		
	}
}
