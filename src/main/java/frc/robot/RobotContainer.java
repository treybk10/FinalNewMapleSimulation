// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.simulation.SimulatedArena;
import frc.robot.simulation.drivesims.SwerveDriveSimulation;
import frc.robot.simulation.drivesims.configs.DriveTrainSimulationConfig;
import frc.robot.simulation.seasonspecific.rebuilt2026.RebuiltFuelOnField;
import frc.robot.subsystems.ExampleSubsystem;

import java.lang.System.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private final SwerveDriveSimulation m_swerveSim;
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem;

  SimulatedArena arena = SimulatedArena.getInstance();
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    
    // Configure the trigger bindings
    m_swerveSim = new SwerveDriveSimulation(
        DriveTrainSimulationConfig.Default(), // Replace with your actual config variable
        new Pose2d()                      // Starting position
    );
    arena.addDriveTrainSimulation(m_swerveSim);

    m_exampleSubsystem  = new ExampleSubsystem(m_swerveSim);
    // SimulatedArena.getInstance().addGamePiece(
    // new RebuiltFuelOnField(new Translation2d(8.2, 4.1)) 
    // );
    for (int i = 0; i < 20; i++) {
      double x = 7.0 + (Math.random() * 2.0); // Random x between 7 and 9
      double y = 3.0 + (Math.random() * 2.0); // Random y between 3 and 5
      
      SimulatedArena.getInstance().addGamePiece(
          new RebuiltFuelOnField(new Translation2d(x, y))
      );
    }

    
    // SimulatedArena.getInstance().addGamePiece(new RebuiltFuelOnField(new Translation2d(2,2)));
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    new Trigger(m_exampleSubsystem::exampleCondition)
        .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_exampleSubsystem.setDefaultCommand(
        m_exampleSubsystem.run(() -> {
            // Read controller axes (Left stick for movement, Right X for rotation)
            double xSpeed = -m_driverController.getLeftY();
            double ySpeed = -m_driverController.getLeftX();
            double rot = -m_driverController.getLeftTriggerAxis();

            // apply these speeds to your drivetrain
            // This is where you call your drive method!
            m_exampleSubsystem.drive(xSpeed, ySpeed, rot); 
        })
    );
    m_driverController.a().onTrue(
      m_exampleSubsystem.runOnce(() -> {
          // Snap the physics body back to (0, 0)
          m_swerveSim.setSimulationWorldPose(new Pose2d(5, 5, new Rotation2d()));
      })
    );
    m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());

    m_driverController.x().onTrue(
      m_exampleSubsystem.runOnce(() -> {
          // Snap the physics body back to (0, 0)
           SimulatedArena.getInstance().addGamePiece(
    new RebuiltFuelOnField(new Translation2d(8.2, 4.1)) 
);
      })
    );
    // Logger.recordOutput("FieldSimulation/Fuel", 
    // SimulatedArena.getInstance().getGamePiecesArrayByType("Fuel"));
    // SimulatedArena.getInstance().addGamePiece(new RebuiltFuelOnField(new Translation2d(2,2)));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }
  public SwerveDriveSimulation getDriveSim() {
    return m_swerveSim; // Or whatever your sim variable is named here
}
}
