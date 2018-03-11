package org.usfirst.frc.team6868.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Autonomous {								//Class that holds autonomous systems
	
	public Autonomous() {
		
	}
	double time = 0;									//Time remaining in seconds
	int tick = 0;										//Time by 'ticks'
	double clawMove = 0;								//Value for the claw movement for limit switch implementation
	
	public void run() {
		time = Timer.getMatchTime();
		tick = 1500 - ((int) (time * 100));				//Calculate current tick - Gets ~0-1499
														//Give a 5 tick interval for setting to be sure
		
		RRAuto();
		
		if(clawMove == 0) {							//Handle claw movement (limit switches)
			OI.clawMotorL.setSpeed(0);
			OI.clawMotorR.setSpeed(0);
		} else if(clawMove > 0) {
			if(!OI.limitHigh.get()) {
				OI.clawMotorL.setSpeed(clawMove);
				OI.clawMotorR.setSpeed(-clawMove);
			} else {
				OI.clawMotorL.setSpeed(0);
				OI.clawMotorR.setSpeed(0);
			}
		} else {
			if(!OI.limitLow.get()) {
				OI.clawMotorL.setSpeed(clawMove);
				OI.clawMotorR.setSpeed(-clawMove);
			} else {
				OI.clawMotorL.setSpeed(0);
				OI.clawMotorR.setSpeed(0);
			}
		}
	}
	
	public void RRAuto() {								//Auto for starting on the right and going for the right
		if(tick >= 10 && tick <= 15) {					//Forward
			OI.leftMotor.setSpeed(0.7);
			OI.rightMotor.setSpeed(-0.7);
		} else if(tick >= 215 && tick <= 220) {			//Right
			OI.leftMotor.setSpeed(0.5);
			OI.rightMotor.setSpeed(0.5);
		} else if(tick >= 325 && tick <= 330) {			//Move back a bit
			OI.leftMotor.setSpeed(-0.5);
			OI.rightMotor.setSpeed(0.5);
		} else if(tick >= 375 && tick <= 380) {			//Stop, start claw movement
			OI.leftMotor.setSpeed(0);
			OI.rightMotor.setSpeed(0);
			clawMove = 0.7;
		} else if(tick >= 430 && tick <= 435) {			//Stop claw movement
			clawMove = 0;
		} else if(tick >= 440 && tick <= 445) {			//Open claw
			OI.claw.set(DoubleSolenoid.Value.kForward);
		} else if(tick >= 490 && tick <= 495) {			//Close claw
			OI.claw.set(DoubleSolenoid.Value.kReverse);
		} else if(tick >= 500 && tick <= 505) {			//Forward/right
			OI.leftMotor.setSpeed(1);
			OI.rightMotor.setSpeed(0.3);
		} else if(tick >= 600 && tick <= 605) {			//Forward
			OI.leftMotor.setSpeed(0.7);
			OI.rightMotor.setSpeed(-0.7);
		} else if(tick >= 650 && tick <= 655) {			//Forward/right
			OI.leftMotor.setSpeed(0.7);
			OI.rightMotor.setSpeed(0.3);
		} else if(tick >= 725 && tick <= 730) {			//Forward
			OI.leftMotor.setSpeed(0.7);
			OI.rightMotor.setSpeed(-0.7);
		} else if(tick >= 780 && tick <= 785) {			//Stop
			OI.leftMotor.setSpeed(0);
			OI.rightMotor.setSpeed(0);
		}
	}
}
