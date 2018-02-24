package com.hiltonrobotics.steamworksbot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class TeleopControl {
	
	static String gameData = "";											//Game data string
	static double controlX = 0;												//X-Axis of left joystick
	static double controlY = 0;												//Y-Axis of left joystick
	static double controlLT = 0;											//Value for LT
	static double controlRT = 0;											//Value for RT
	static double controlThrottle = 0;										//Throttle-Axis of the right joystick
	static double moveSpeed = 0.7;											//Movement motor speed multiplier
	static double clawSpeed = 0.8;											//Claw movement motor speed mutiplier
	static double clawMoveTrimSpeed = 0.15;									//Claw movement trim speed
	static double liftSpeed = 0.7;											//Multiplier for the lift speed
	static double clawSafeSpeed = 0.2;										//Speed for the claw to correct at limit switches
	static int dpad = 0;													//Value for the dpad 'angle'
	static int liftStatus = 0;												//Number for what state the lift is in
	static int liftStatusPrev = -1;
	static boolean dpadUp = false;											//D-pad up button
	static boolean dpadDown = false;										//D-pad down button
	static boolean dpadLeft = false;										//D-pad left button
	static boolean dpadRight = false;										//D-pad right button
	static boolean clawOpen = false;
	static boolean clawOpenPrev = false;
	static boolean winchSide = true;										//Winch mode - False = velcro
	static boolean newBButtonPress = true;
	static boolean altController = false;									//True if using alt controller
	
	public static void runPeriodic() {
		setInputs();														//Sets input variables
		resetMotors();														//Resets motors
		setClaw();															//Sets claw/arm position
		setDriveMotors();													//Set drive motors
		setLiftMotors();													//Set lift motors
		setLiftPosition();
		printStatuses();													//Print device statuses such as PDP voltage
	}
	
	public static void printStatuses() {
		System.out.println("PDP Voltage: " + OI.pdp.getVoltage());
		System.out.println("B: " + OI.buttonB.get());
	}
	
	public static void setLiftPosition() {									//Set which winch is being used
		if(OI.buttonB.get() && newBButtonPress) {
			newBButtonPress = false;
			liftStatus++;
		} else if(!OI.buttonB.get() && !newBButtonPress) {
			newBButtonPress = true;
		}
		
		if(liftStatusPrev != liftStatus) {
			liftStatusPrev = liftStatus;
			
			if(liftStatus == 0) { // Red = 2 black = 1
				OI.lift1.set(true);
				OI.lift2.set(false);
			} else if(liftStatus == 1) {
				OI.lift1.set(false);
				OI.lift2.set(true);
			} else if(liftStatus == 2) {
				OI.lift1.set(true);
				OI.lift2.set(false);
			} else if(liftStatus != -1) {
				liftStatus = -1;
			}
		}
			
			//First: 2 and 3 don't have air
			//Second: 3 gets air
			//Thrid (at end) 2 gets air
	}
	
	public static void setLiftMotors() {									//Set lift motors
		if(altController) {
			if(OI.buttonLB.get()) {
				OI.liftMotor1.setSpeed(liftSpeed);
				OI.liftMotor2.setSpeed(liftSpeed);
			} /*else if(OI.controller.getRawAxis(2) > 0.1){
				OI.liftMotor1.setSpeed(-liftSpeed);
				OI.liftMotor2.setSpeed(-liftSpeed);
			}*/
		} else {
			if(OI.buttonLB.get()) {												
				OI.liftMotor1.setSpeed(liftSpeed);
				OI.liftMotor2.setSpeed(liftSpeed);
			} /*else if(OI.buttonLT.get()) {
				OI.liftMotor1.setSpeed(-liftSpeed);
				OI.liftMotor2.setSpeed(-liftSpeed);
			}*/
		}
	}
	
	public static void setDriveMotors() {									//Set drive motors
		if(Math.abs(controlX) > Math.abs(controlY)) {						//Apply whichever axis is of greater absolute value
			OI.leftMotor.setSpeed(controlX * moveSpeed);					//Turn robot
			OI.rightMotor.setSpeed(controlX * moveSpeed);					//TODO: test and swap both to negative if wrong way
		} else {
			OI.leftMotor.setSpeed(-(controlY * moveSpeed));					//Move robot forwards/backwards
			OI.rightMotor.setSpeed(controlY * moveSpeed);				//TODO: test and swap negative to leftMotor if wrong way
		}
	}
	
	public static void setClaw() {
		if(clawOpenPrev != clawOpen) {
			if(clawOpen) {														//Set claw open/closed
				OI.claw.set(DoubleSolenoid.Value.kForward);
			} else {
				OI.claw.set(DoubleSolenoid.Value.kReverse);
			}
			
			clawOpenPrev = clawOpen;
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
	
	public static void trimClaw() {											//Trim claw arm position - separate control over arm motors
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
	
	public static void resetMotors() {										//Reset motors and solenoids
		OI.leftMotor.setSpeed(0);
		OI.rightMotor.setSpeed(0);
		OI.clawMotorL.setSpeed(0);
		OI.clawMotorR.setSpeed(0);
		OI.liftMotor1.setSpeed(0);
		OI.liftMotor2.setSpeed(0);
		OI.claw.set(DoubleSolenoid.Value.kOff);
	}
	
	public static void setInputs() {										//Sets variables at the start of a tick
		gameData = DriverStation.getInstance().getGameSpecificMessage();	//Get game data string
		
		if(altController) {													//Alternate controller
			OI.controller = new Joystick(1);
			controlX = OI.controller.getRawAxis(0);
			controlY = OI.controller.getRawAxis(1);
			OI.buttonA = new JoystickButton(OI.controller, 1);
			OI.buttonB = new JoystickButton(OI.controller, 2);
			OI.buttonLB = new JoystickButton(OI.controller, 5);
			controlThrottle = OI.controller.getRawAxis(5);
			clawOpen = (OI.controller.getRawAxis(3) > 0.1);
		} else {															//Normal controller
			OI.controller = new Joystick(0);
			OI.buttonLB = new JoystickButton(OI.controller, 5);
			OI.buttonA = new JoystickButton(OI.controller, 0);
			OI.buttonB = new JoystickButton(OI.controller, 3);
			controlX = OI.controller.getX();								//Get X-Axis
			controlY = OI.controller.getY();								//Get Y-Axis
			controlThrottle = OI.controller.getThrottle();					//Get Throttle-Axis
			clawOpen = OI.buttonRT.get();										//Open claw when RT pressed
		}
		
		dpad = OI.controller.getPOV();										//Get D-pad angle
		dpadUp = (dpad == 0 || dpad == 315 || dpad == 45);					//Set D-pad sides to separate values
		dpadDown = (dpad == 135 || dpad == 180 || dpad == 225);
		dpadLeft = (dpad == 225 || dpad == 270 || dpad == 315);
		dpadRight = (dpad == 45 || dpad == 90 || dpad == 135);
		
		if(controlThrottle == -0.0078125) controlThrottle = 0;				//Correct right joystick - rest position was showing -0.0078125
	}
	
	public static void runInit() {
		OI.calibrateGyroSafe();
	}
}