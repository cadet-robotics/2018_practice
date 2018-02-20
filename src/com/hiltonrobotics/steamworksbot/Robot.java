/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.hiltonrobotics.steamworksbot;

import edu.wpi.first.networktables.NetworkTable;
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
	
	public static Robot instance = null;
	@Override
	public void robotInit() {
		/*m_oi = new OI();
		m_chooser.addDefault("Default Auto", new ExampleCommand());*/
		// chooser.addObject("My Auto", new MyAutoCommand());
		//SmartDashboard.putData("Auto mode", m_chooser);
		camManager = AutoCamManager.getInstance();
		if (instance == null) {
			instance = this;
		}
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
		AutoControl.init();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		//Scheduler.getInstance().run();
		AutoControl.runPeriodic();
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
	
	@Override
	public void teleopPeriodic() {
		//Scheduler.getInstance().run();
		//System.out.println(String.valueOf(OI.sensorDist.distMM()));
		//OI.servo.setAngle(OI.joystick.getY() * 10 + 90);
		//OI.servo.setAngle(OI.joystick.getY() * 4000);
		
		//periodicGyro();
		
		//OI.motor.setSpeed(0.5);
		//System.out.println(OI.panel.getTotalPower());
		
		TeleopControl.runPeriodic();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
