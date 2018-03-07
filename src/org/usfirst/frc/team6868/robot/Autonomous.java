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
		
		if(tick >= 10 && tick <= 15) {					//100 ticks = 1 second
			OI.leftMotor.setSpeed(0.8);					//Give a 5 tick interval for setting to be sure
			OI.rightMotor.setSpeed(-0.6);				//Forward/right
		} else if(tick >= 100 && tick <= 105) {			//Forward/left
			OI.leftMotor.setSpeed(0.5);
			OI.rightMotor.setSpeed(-0.85);
		} else if(tick >= 215 && tick <= 220) {			//Right
			OI.leftMotor.setSpeed(0.6);
			OI.rightMotor.setSpeed(0.6);
		} else if(tick >= 325 && tick <= 330) {			//Stop, start claw movement
			OI.leftMotor.setSpeed(0);
			OI.rightMotor.setSpeed(0);
			clawMove = 0.7;
		} else if(tick >= 380 && tick <= 385) {			//Stop claw movement
			clawMove = 0;
		} else if(tick >= 390 && tick <= 395) {			//Open claw
			OI.claw.set(DoubleSolenoid.Value.kForward);
		} else if(tick >= 440 && tick <= 445) {			//Close claw
			OI.claw.set(DoubleSolenoid.Value.kReverse);
		}
		
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
}
