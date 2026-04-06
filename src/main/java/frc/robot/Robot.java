// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.System.Logger;
import java.util.Arrays;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.simulation.SimulatedArena;
import frc.robot.simulation.drivesims.SwerveDriveSimulation;
import frc.robot.simulation.seasonspecific.rebuilt2026.RebuiltFuelOnField;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private SwerveDriveSimulation m_driveSim; 
  private Command m_autonomousCommand;
  private final Field2d m_fuelField = new Field2d();

  StructArrayPublisher<Pose3d> m_fuelPosesPublisher = NetworkTableInstance.getDefault()
    .getStructArrayTopic("MyPoseArray", Pose3d.struct)
    .publish();

  private RobotContainer m_robotContainer;
    private final Field2d m_field = new Field2d();
  
    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    public Robot() {
      // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
      // autonomous chooser on the dashboard.
      m_robotContainer = new RobotContainer();
    }
  
    /**
     * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
      // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
      // commands, running already-scheduled commands, removing finished or interrupted commands,
      // and running subsystem periodic() methods.  This must be called from the robot's periodic
      // block in order for anything in the Command-based framework to work.
      CommandScheduler.getInstance().run();
      //SmartDashboard.putData("Driver Controller", m_robotContainer.getDriverController().getHID());
      // // 1. Get the pose from the MapleSim engine
      // Pose2d simPose = m_driveSim.getSimulatedDriveTrainPose(); 
      
      // // 2. Set the 'Field' widget to that pose
      // m_field.setRobotPose(simPose);
      
      // // 3. Publish it to NetworkTables so AdvantageScope can see it
      // SmartDashboard.putData("Field", m_field);
  
  
  
  
      // Pose2d realSimPose = m_driveSim.getSimulatedDriveTrainPose();
  
      // m_field.setRobotPose(realSimPose);
      // SmartDashboard.putData("Field", m_field);
    }
  
    @Override
    public void robotInit() {
      // Your initialization code here
      m_robotContainer = new RobotContainer();
    m_driveSim = m_robotContainer.getDriveSim(); 
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
    //m_driveSim = m_robotContainer.getDriveSim(); 
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
    SimulatedArena.getInstance().simulationPeriodic();
    // 1. Update the robot pose (Already looks good)
    if (m_driveSim != null) {
        m_field.setRobotPose(m_driveSim.getSimulatedDriveTrainPose());
        SmartDashboard.putData("Field", m_field);
    }

    // 2. Get the fuel poses from the arena as an array
    Pose3d[] fuelArray = SimulatedArena.getInstance()
            .getGamePiecesArrayByType("Fuel");

    // 3. Use .set() on the PUBLISHER (m_fuelPosesPublisher), not the array
    m_fuelPosesPublisher.set(fuelArray);
  }
}
