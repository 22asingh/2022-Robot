package frc.robot.commands.auto.lib;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivetrainSubsystem;

import static frc.robot.Constants.DriveConstants.maxAutoSpeed;

public class AutoForwardDistance extends CommandBase {
    DrivetrainSubsystem drivetrain;
    private final double metersToDrive;
    private final double Kp = 0.9;
    private final double Ki = 0.0;
    private final double Kd = 0.05;
    private double setpoint = 0.0;
    PIDController pid = new PIDController(Kp, Ki, Kd);

    public AutoForwardDistance(DrivetrainSubsystem drivetrainSubsystem, double metersToDrive) {
        this.metersToDrive = metersToDrive;
        this.drivetrain = drivetrainSubsystem;
        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void initialize() {
        this.setpoint = drivetrain.getRightDistanceDriven() + metersToDrive;
        pid.setTolerance(0.02);
        pid.setSetpoint(setpoint);
    }

    @Override
    public void execute() {
        double drivePower = Math.min(pid.calculate(drivetrain.getRightDistanceDriven()), maxAutoSpeed);
        //drivePower = Math.abs(drivePower) < 0.06 ? 0 : drivePower;
        System.out.println(drivePower);
        drivetrain.autoTankDrive(-drivePower, drivePower);
    }

    @Override
    public boolean isFinished() {
        return pid.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.autoTankDrive(0, 0);
    }}
