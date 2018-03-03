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
	static double moveSpeedForwards = 0.7;									//Forwards-movement motor speed multiplier
	static double moveSpeedTurn = 0.5;										//Turning-movement motor speed multiplier
	static double leftMotorSpeed = 0;										//Absolute value for left motor speed
	static double rightMotorSpeed = 0;										//Absolute value for right motor speed
	static double clawSpeed = 0.8;											//Claw movement motor speed mutiplier
	static double clawMoveTrimSpeed = 0.15;									//Claw movement trim speed
	static double liftSpeed = 0.7;											//Multiplier for the lift speed
	static double clawSafeSpeed = 0.2;										//Speed for the claw to correct at limit switches
	static double rampPercent = 0.25;										//Power multiplier for drive motors - Ramps up while held
	static double rampIncr = 0.05;											//Linear increment for ramping
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
	static boolean twoControllers = true;									//True if using two controllers
	static boolean useX = false;											//Use Y-axis in movement (set each tick)
	static boolean useY = false;											//Use X-axis in movement (set each tick)
	
	public static void runPeriodic() {
		setInputs();														//Sets input variables
		resetMotors();														//Resets motors
		setClaw();															//Sets claw/arm position
		setDriveMotors();													//Set drive motors
		setLiftMotors();													//Set lift motors
		setLiftPosition();
		printStatuses();													//Print device statuses such as PDP voltage
	}
	
	public static void printStatuses() {									//Console output for any statuses the drivers would need, currently lift mode
		if(liftStatus == 0) {
			System.out.println("Lift State: Reset");
		} else if(liftStatus == 1) {
			System.out.println("List State: Lift Hook");
		} else if(liftStatus == 2) {
			System.out.println("Lift State: Lift Robot");
		} else if(liftStatus == -1) {
			System.out.println("Lift State: Lock Robot");
		}
	}
	
	public static void setLiftPosition() {									//Set which winch is being used
		if(OI.buttonB.get() && newBButtonPress) {							//Get separate presses of the B button
			newBButtonPress = false;
			liftStatus++;
		} else if(!OI.buttonB.get() && !newBButtonPress) {
			newBButtonPress = true;
		}
		
		if(liftStatusPrev != liftStatus) {									//Change solenoid if it needs to be changed, don't set every tick
			liftStatusPrev = liftStatus;
			
			if(liftStatus == 0) { // Red = 2 black = 1
				OI.lift1.set(false);
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
	}
	
	public static void setLiftMotors() {									//Set lift motors
		if(altController) {													//Alternate controller setup
			if(OI.buttonLB.get()) {
				OI.liftMotor1.setSpeed(liftSpeed);
				OI.liftMotor2.setSpeed(liftSpeed);
				OI.liftMotor3.setSpeed(liftSpeed);
			} else if(OI.controller.getRawAxis(2) > 0.1){					//Reverse lift
				OI.liftMotor1.setSpeed(-liftSpeed);
				OI.liftMotor2.setSpeed(-liftSpeed);
				OI.liftMotor3.setSpeed(-liftSpeed);
			}
		} else {															//Normal controller setup
			if(OI.buttonLB.get()) {												
				OI.liftMotor1.setSpeed(liftSpeed);
				OI.liftMotor2.setSpeed(liftSpeed);
				OI.liftMotor3.setSpeed(liftSpeed);
			} else if(OI.buttonLT.get()) {									//Reverse lift
				OI.liftMotor1.setSpeed(-liftSpeed);
				OI.liftMotor2.setSpeed(-liftSpeed);
				OI.liftMotor3.setSpeed(-liftSpeed);
			}
		}
	}
	
	public static void setDriveMotors() {									//Set drive motors
																			//Old Calculation
		/*if(Math.abs(controlX) > Math.abs(controlY)) {						//Apply whichever axis is of greater absolute value
			OI.leftMotor.setSpeed(controlX * moveSpeed * rampPercent);		//Turn robot
			OI.rightMotor.setSpeed(controlX * moveSpeed * rampPercent);
		} else {
			OI.leftMotor.setSpeed(-(controlY * moveSpeed * rampPercent));	//Move robot forwards/backwards
			OI.rightMotor.setSpeed(controlY * moveSpeed * rampPercent);
		}
		*/
		
																			//New method for calculating movement speed - Allows turning while moving forwards/backwards
		leftMotorSpeed = 0;													//Reset variables for calculation
		rightMotorSpeed = 0;
		useY = (Math.abs(controlY) >= 0.1);									//Use only X or only Y when the other is too low a value - Allow for easy fully straight or fully turning driving
		useX = (Math.abs(controlX) >= 0.1);									//Uses both if both are of a high enough value
		
		if(!useX && !useY) {												//Use both if neither are of a high enough value
			useX = true;
			useY = true;
		}
		
		if(useX) {															//Calculate motor speeds, adds for each axis if used
			leftMotorSpeed += controlX * moveSpeedTurn * rampPercent;
			rightMotorSpeed += controlX * moveSpeedTurn * rampPercent;
		}
		if(useY) {
			leftMotorSpeed -= controlY * moveSpeedForwards * rampPercent;
			rightMotorSpeed += controlY * moveSpeedForwards * rampPercent;
		}
		
		OI.leftMotor.setSpeed(leftMotorSpeed);								//Set motor speeds
		OI.rightMotor.setSpeed(rightMotorSpeed);
	}
	
	public static void setClaw() {
		if(clawOpenPrev != clawOpen) {										//Only set solenoids when needed, don't set every frame
			if(clawOpen) {													//Set claw open/closed
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
	
	public static void trimClaw() {											//Trim claw arm position - separate control over arm motors - May be deprecated
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
	
	public static void resetMotors() {										//Reset motors
		OI.leftMotor.setSpeed(0);
		OI.rightMotor.setSpeed(0);
		OI.clawMotorL.setSpeed(0);
		OI.clawMotorR.setSpeed(0);
		OI.liftMotor1.setSpeed(0);
		OI.liftMotor2.setSpeed(0);
	}
	
	public static void setInputs() {										//Sets variables at the start of a tick
		gameData = DriverStation.getInstance().getGameSpecificMessage();	//Get game data string
		
		if(altController) {													//Alternate controller setup
			OI.controller = new Joystick(1);
			controlX = OI.controller.getRawAxis(0);
			controlY = OI.controller.getRawAxis(1);
			OI.buttonA = new JoystickButton(OI.controller, 1);
			OI.buttonB = new JoystickButton(OI.controller, 2);
			OI.buttonLB = new JoystickButton(OI.controller, 5);
			controlThrottle = OI.controller.getRawAxis(5);
			clawOpen = (OI.controller.getRawAxis(3) > 0.1);
		} else {															//Normal controller setup
			OI.controller = new Joystick(0);
			OI.controller2 = new Joystick(1);
			if(twoControllers) {
				OI.buttonLB = new JoystickButton(OI.controller2, 5);
				OI.buttonB = new JoystickButton(OI.controller2, 3);
			} else {
				OI.buttonLB = new JoystickButton(OI.controller, 5);
				OI.buttonB = new JoystickButton(OI.controller, 3);
			}
			OI.buttonA = new JoystickButton(OI.controller, 0);
			controlX = OI.controller.getX();								//Get X-Axis
			controlY = OI.controller.getY();								//Get Y-Axis
			controlThrottle = OI.controller.getThrottle();					//Get Throttle-Axis
			clawOpen = OI.buttonRT.get();									//Open claw when RT pressed
		}
		
		dpad = OI.controller.getPOV();										//Get D-pad angle
		dpadUp = (dpad == 0 || dpad == 315 || dpad == 45);					//Set D-pad sides to separate values
		dpadDown = (dpad == 135 || dpad == 180 || dpad == 225);
		dpadLeft = (dpad == 225 || dpad == 270 || dpad == 315);
		dpadRight = (dpad == 45 || dpad == 90 || dpad == 135);
		
		if(controlThrottle == -0.0078125) controlThrottle = 0;				//Correct right joystick - rest position was showing -0.0078125
		
		if(controlX != 0 || controlY != 0) {								//Motor ramping: start at 25% speed
			if(rampPercent < 1) {
				rampPercent += rampIncr;
			} else {
				rampPercent = 1;
			}
		} else {
			rampPercent = 0.25;
		}
	}
	
	public static void runInit() {											//Calibrate Owen's gyroscope
		OI.calibrateGyroSafe();
	}
}