package com.hiltonrobotics.steamworksbot;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopControl {
	
	static final int LIFT_HOOK_STATE = 0;
	static final int LIFT_ROBOT_STATE = 2;
	
	static String gameData = "";											//Game data string
	static double controlX = 0;												//X-Axis of left joystick
	static double controlY = 0;												//Y-Axis of left joystick
	static double controlLT = 0;											//Value for LT
	static double controlRT = 0;											//Value for RT
	static double controlThrottle = 0;										//Throttle-Axis of the right joystick
	static double moveSpeedForwards = 0.85;									//Forwards-movement motor speed multiplier
	static double moveSpeedTurn = 0.7;										//Turning-movement motor speed multiplier
	static double leftMotorSpeed = 0;										//Absolute value for left motor speed
	static double rightMotorSpeed = 0;										//Absolute value for right motor speed
	static double clawSpeed = 1;											//Claw movement motor speed mutiplier
	static double clawMoveTrimSpeed = 0.15;									//Claw movement trim speed
	static double liftSpeed = 0.7;											//Multiplier for the lift speed
	static double clawSafeSpeed = 0.2;										//Speed for the claw to correct at limit switches
	static double rampPercent = 0.25;										//Power multiplier for drive motors - Ramps up while held
	static double rampIncr = 0.05;											//Linear increment for ramping
	static int dpad = 0;													//Value for the dpad 'angle'
	static int liftStatus = LIFT_HOOK_STATE;								//Number for what state the lift is in
	static int liftStatusPrev = -1;											//Previous state of the lift
	static int cubeEjectTimer = 0;											//Value to allow the claw motors to run after the button has been tapped
	static int cubeIndicatorTimer = 0;										//Value to allow the cube indicator rumble for more than 1 tick
	static boolean dpadUp = false;											//D-pad up button
	static boolean dpadDown = false;										//D-pad down button
	static boolean dpadLeft = false;										//D-pad left button
	static boolean dpadRight = false;										//D-pad right button
	static boolean clawOpen = false;										//Weather or not the claw is open
	static boolean clawOpenPrev = false;									//Previous state of the claw
	static boolean gettingCube = false;										//Getting the cube or not
	static boolean ejectingCube = false;									//Ejecting the cube or not
	static boolean cubeIndicated = false;									//Weather or not the cube being in the claw has been indicated
	static boolean buttonLTPressed = false;									//If LT is pressed
	static boolean buttonLT2Pressed = false;								//If LT is pressed on the other controller
	static boolean buttonRTPressed = false;									//If RT is pressed
	static boolean newBButtonPress = true;									//Weather or not the b-button being pressed is a new press
	static boolean newXButtonPress = true;									//Weather or not the x-button being pressed is a new press
	static boolean newRTPress = true;										//Weather or not RT being pressed is a new press
	static boolean newRBPress = true;										//Weather or not RB being pressed is a new press
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
		setLiftPosition();													//Manages lift state
		manageClaw();														//Manages semi-auto cube getting
		printStatuses();													//Print device statuses such as PDP voltage
	}
	
	public static void printStatuses() {									//Console output for any statuses the drivers would need, currently lift mode
		DecimalFormat f = new DecimalFormat("#.###");
		f.setRoundingMode(RoundingMode.HALF_UP);
		
		double pcx = Double.parseDouble(f.format(controlX));
		double pcy = Double.parseDouble(f.format(controlY));
		double pct = Double.parseDouble(f.format(controlThrottle));
		String armDir = (pct >= 0) ? "Down" : "Up";
		
		if(liftStatus == LIFT_HOOK_STATE) {
			SmartDashboard.putString("Lift State",  "Lift Hook");
		} else if(liftStatus == LIFT_ROBOT_STATE) {
			SmartDashboard.putString("Lift State", "Lift Robot");
		}
		
		SmartDashboard.putNumber("PC X", pcx);
		SmartDashboard.putNumber("PC Y", pcy);
		SmartDashboard.putNumber("Arm Speed", pct);
		SmartDashboard.putString("Arm Direction", armDir);
		SmartDashboard.putBoolean("Claw Status", clawOpen);
	}
	
	public static void manageClaw() {										//Manages the semi-automated cube-getting
		/*
		if(buttonRTPressed && newRTPress) {									//Set getting cube
			newRTPress = false;
			
			if(!ejectingCube) {
				gettingCube = !gettingCube;
			}
		} else if(!buttonRTPressed && !newRTPress) {
			newRTPress = true;
		}*/
		
		gettingCube = buttonRTPressed && !ejectingCube;
		
		if(OI.buttonRB.get() && newRBPress) {								//Set ejecting cube
			newRBPress = false;
			
			ejectingCube = !gettingCube;
			cubeEjectTimer = 20;
		} else if(!OI.buttonRB.get() && !newRBPress) {
			newRBPress = true;
		}
		
		if(gettingCube) {													//Get cube
			OI.cubeMotor.setSpeed(-1);
		} else if(ejectingCube) {											//Eject cube
			/*if(OI.limitCube.getVoltage() <= 1){
				cubeEjectTimer = 20;
			} else */if(cubeEjectTimer <= 0) {
				ejectingCube = false;
			}
			
			cubeEjectTimer--;
			
			OI.cubeMotor.setSpeed(1);
		}
		
		if(OI.limitCube.getVoltage() >= 3 && clawOpen) {
			OI.controller.setRumble(GenericHID.RumbleType.kLeftRumble, 1);
			OI.controller.setRumble(GenericHID.RumbleType.kRightRumble, 1);				
		} else {
			OI.controller.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
			OI.controller.setRumble(GenericHID.RumbleType.kRightRumble, 0);
		}
	}
	
	public static void setLiftPosition() {									//Set which winch is being used
		if(OI.buttonB.get() && newBButtonPress) {							//Get separate presses of the B button
			newBButtonPress = false;
			if (liftStatus != LIFT_ROBOT_STATE) {
				liftStatus = LIFT_ROBOT_STATE;
			}
		} else if(!OI.buttonB.get() && !newBButtonPress) {
			newBButtonPress = true;
		}
		
		if(OI.buttonX.get() && newXButtonPress) {							//Get separate presses of the B button
			newXButtonPress = false;
			if(liftStatus != LIFT_HOOK_STATE) {
				liftStatus = LIFT_HOOK_STATE;
			}
		} else if(!OI.buttonX.get() && !newXButtonPress) {
			newXButtonPress = true;
		}
		
		if(liftStatusPrev != liftStatus) {									//Change solenoid if it needs to be changed, don't set every tick
			liftStatusPrev = liftStatus;
			
			if(liftStatus == LIFT_HOOK_STATE) {
				OI.hookSol.set(true);
				OI.winchSol.set(false);
			} else if(liftStatus == LIFT_ROBOT_STATE) {
				OI.hookSol.set(false);
				OI.winchSol.set(true);
			}
		}
	}
	
	public static void setLiftMotors() {
		if(buttonLTPressed /*&& OI.limitLift.get()*/) {												
			OI.liftMotor1.setSpeed(liftSpeed);
			OI.liftMotor2.setSpeed(liftSpeed);
			OI.liftMotor3.setSpeed(liftSpeed);
		} else if(OI.buttonLB.get() /*&& OI.limitLift.get()*/) {		//Reverse lift
			OI.liftMotor1.setSpeed(-liftSpeed);
			OI.liftMotor2.setSpeed(-liftSpeed);
			OI.liftMotor3.setSpeed(-liftSpeed);
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
		
		if(controlThrottle < 0) {											//Move arm where pulling the stick towards yourself lowers the arm
			if(!OI.limitHigh.get()) {										//Only allow movement in the direction opposite a pressed limit switch
				OI.clawMotorL.setSpeed(-(controlThrottle * clawSpeed));
				OI.clawMotorR.setSpeed(controlThrottle * clawSpeed);
			}
		} else {
			if(!OI.limitLow.get()) {
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
		OI.liftMotor3.setSpeed(0);
		OI.cubeMotor.setSpeed(0);
	}
	
	public static void setInputs() {										//Sets variables at the start of a tick
		gameData = DriverStation.getInstance().getGameSpecificMessage();	//Get game data string
		
		OI.controller = new Joystick(0);
		OI.controller2 = new Joystick(1);
		OI.buttonLB = new JoystickButton(OI.controller2, 5);
		//OI.buttonLT = new JoystickButton(OI.controller2, 7);
		buttonLTPressed = OI.controller2.getRawAxis(2) >= 0.1;
		buttonLT2Pressed = OI.controller.getRawAxis(2) >= 0.1;
		buttonRTPressed = OI.controller.getRawAxis(3) >= 0.1;
		OI.buttonB = new JoystickButton(OI.controller2, 2);
		OI.buttonX = new JoystickButton(OI.controller2, 3);
		OI.buttonA = new JoystickButton(OI.controller, 1);
		controlX = OI.controller.getRawAxis(0);								//Get X-Axis
		controlY = OI.controller.getRawAxis(1);								//Get Y-Axis
		controlThrottle = OI.controller.getRawAxis(5);						//Get Throttle-Axis
		clawOpen = buttonLT2Pressed;										//Open claw when RT pressed (Depricated with new system)
		
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
}