// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.simulation.drivesims.SwerveDriveSimulation;

public class ExampleSubsystem extends SubsystemBase {
  private SwerveDriveSimulation drive;
  /** Creates a new ExampleSubsystem. */
  public ExampleSubsystem(SwerveDriveSimulation drive) {
    this.drive = drive;
  }

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here */
        });
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  public void drive(double xSpeed, double ySpeed, double rot) {
    if (drive != null) {
      // 1. Create ChassisSpeeds (meters per second)
      // Multiply by a max speed (e.g., 4.0) so it's not just 1 m/s
      ChassisSpeeds speeds = new ChassisSpeeds(xSpeed * 4.0, ySpeed * 4.0, rot * 10.0);
      
      // 2. Pass those speeds to the MapleSim engine
      drive.setRobotSpeeds(speeds);
      System.out.println(xSpeed);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
