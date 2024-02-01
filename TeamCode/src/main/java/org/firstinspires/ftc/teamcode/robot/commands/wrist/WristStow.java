package org.firstinspires.ftc.teamcode.robot.commands.wrist;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.util.Timing;

import org.firstinspires.ftc.teamcode.robot.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.robot.subsystems.WristSubsystem;

import java.util.concurrent.TimeUnit;

public class WristStow extends CommandBase {
    private final int TIME_TO_POSITION = 1000;
    private final WristSubsystem wristSubsystem;
    Timing.Timer timer;
    public WristStow(WristSubsystem subsystem)
    {
        this.timer = new Timing.Timer(TIME_TO_POSITION, TimeUnit.MILLISECONDS);
        wristSubsystem = subsystem;
        addRequirements(wristSubsystem);
    }

    @Override
    public void initialize() {
        timer.start();
        wristSubsystem.setWristStow();
    }

    @Override
    public boolean isFinished() {
        return timer.done();
    }
}
