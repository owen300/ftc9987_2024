package org.firstinspires.ftc.teamcode.robot.commands.claw;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.robot.subsystems.ClawSubsystem;

/**
 * open claw
 * waits till the distance sensor detects somethings
 * closes claw
 */
public class ClawAutoCommand extends CommandBase
{
    private final ClawSubsystem clawSubsystem;
    public ClawAutoCommand(ClawSubsystem subsystem)
    {
        clawSubsystem = subsystem;
        addRequirements(clawSubsystem);
    }

    @Override
    public void initialize() {
        clawSubsystem.openBoth();
    }
    @Override
    public void execute(){
        if(clawSubsystem.pixelDetectedLeft())clawSubsystem.closeLeft();
        if(clawSubsystem.pixelDetectedRight())clawSubsystem.closeRight();
    }

    @Override
    public boolean isFinished() {
        return clawSubsystem.pixelDetectedLeft()&& clawSubsystem.pixelDetectedRight();
    }

    @Override
    public void end(boolean interrupted)
    {
        clawSubsystem.close();
    }
}
