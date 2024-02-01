package org.firstinspires.ftc.teamcode.opmode.auto.trajectorysequence;

import static org.firstinspires.ftc.teamcode.robot.commands.claw.ClawOpenCommand.Side.RIGHT;
import static org.firstinspires.ftc.teamcode.robot.commands.tilt.TiltGoToPosition.TELEOP_DEPOSIT;
import static org.firstinspires.ftc.teamcode.robot.commands.tilt.TiltGoToPosition.TELEOP_INTAKE;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.AutoMecanumDrive;
import org.firstinspires.ftc.teamcode.opmode.auto.trajectorysequence.sequencesegment.WaitSegment;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;
import org.firstinspires.ftc.teamcode.robot.commands.StowEverything;
import org.firstinspires.ftc.teamcode.robot.commands.claw.ClawCloseCommand;
import org.firstinspires.ftc.teamcode.robot.commands.claw.ClawOpenCommand;
import org.firstinspires.ftc.teamcode.robot.commands.extension.ExtensionGoToPosition;
import org.firstinspires.ftc.teamcode.robot.commands.tilt.TiltGoToPosition;
import org.firstinspires.ftc.teamcode.robot.commands.wrist.WristDeposit;
import org.firstinspires.ftc.teamcode.robot.commands.wrist.WristIntake;
import org.firstinspires.ftc.teamcode.robot.commands.wrist.WristStow;
import org.firstinspires.ftc.teamcode.robot.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.robot.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.robot.subsystems.ExtensionSubsystem;
import org.firstinspires.ftc.teamcode.robot.subsystems.TiltSubsystem;
import org.firstinspires.ftc.teamcode.robot.subsystems.WristSubsystem;
import org.firstinspires.ftc.teamcode.vision.TeamElementPipeline;
import org.firstinspires.ftc.teamcode.vision.Vision;

@Autonomous(group="auto", name="Auto_BLUE_Far_side")
public class AutoBlueFar extends LinearOpMode
{
    private static final double TILE = 24.0;
    @Override
    public void runOpMode() throws InterruptedException {
        RobotContainer.initiate(hardwareMap, telemetry);
        AutoMecanumDrive drive = new AutoMecanumDrive(hardwareMap);
        //DriveSubsystem driveSubsystem=new DriveSubsystem(hardwareMap);
        ClawSubsystem clawSubsystem = new ClawSubsystem(hardwareMap);
        TiltSubsystem tiltSubsystem = new TiltSubsystem(hardwareMap, telemetry);
        WristSubsystem wristSubsystem = new WristSubsystem(hardwareMap);
        ExtensionSubsystem extensionSubsystem=new ExtensionSubsystem(hardwareMap,telemetry);

        //Owen's shitty ass init code
        //driveSubsystem.init();
        tiltSubsystem.init();
        extensionSubsystem.init();


        Pose2d startPose = new Pose2d(-TILE*1.6, 2.5*TILE, Math.toRadians(-90));

        drive.setPoseEstimate(startPose);

        StowEverything stow = new StowEverything(tiltSubsystem, extensionSubsystem, clawSubsystem, wristSubsystem);

        SequentialCommandGroup place_pixel_and_stow = new SequentialCommandGroup(
                new TiltGoToPosition(tiltSubsystem, TiltGoToPosition.TELEOP_INTAKE),
                new WristIntake(wristSubsystem).withTimeout(300),
                new ClawOpenCommand(clawSubsystem, RIGHT).withTimeout(1000),
                stow);

        SequentialCommandGroup deposit = new SequentialCommandGroup(
                new TiltGoToPosition(tiltSubsystem, TELEOP_DEPOSIT),
                new WristDeposit(wristSubsystem).withTimeout(800),
                new ClawOpenCommand(clawSubsystem, ClawOpenCommand.Side.BOTH).withTimeout(500));

        CommandScheduler.getInstance().schedule(
                new WristStow(wristSubsystem)
        );
        CommandScheduler.getInstance().run();

        TrajectorySequence Center = drive.trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(-TILE*1.5, 1.45*TILE))
                .addTemporalMarker(1.3, () -> {
                    CommandScheduler.getInstance().schedule(place_pixel_and_stow);
                })
                .lineToLinearHeading(new Pose2d(TILE*-1.5, 0.47*TILE, Math.toRadians(-90)))
                .waitSeconds(2)
                .turn(Math.toRadians(90))
                .lineToLinearHeading(new Pose2d(TILE*1.5, 0.5*TILE, Math.toRadians(0)))
                .addDisplacementMarker(() -> {
                    CommandScheduler.getInstance().schedule(deposit);
                })
                .lineToLinearHeading(new Pose2d(TILE*2.35, 1.5*TILE, Math.toRadians(0)))

                .lineToConstantHeading(new Vector2d(TILE*1.9, 2.5*TILE))
                .addDisplacementMarker(() -> {
                    CommandScheduler.getInstance().schedule(new StowEverything(tiltSubsystem, extensionSubsystem, clawSubsystem, wristSubsystem));

                })
                .lineToConstantHeading(new Vector2d(TILE*1.9, 2.499*TILE))
                .build();
                /////////////////


        TrajectorySequence Right = drive.trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(-TILE*1.5, 1.45*TILE))
                .turn(Math.toRadians(110))
                .lineToLinearHeading(new Pose2d(TILE*-1.5, 0.5*TILE, Math.toRadians(0)))
                .lineToLinearHeading(new Pose2d(TILE*1.5, 0.5*TILE, Math.toRadians(0)))
                .lineToLinearHeading(new Pose2d(TILE*2, 1.25*TILE, Math.toRadians(0)))

                .lineToConstantHeading(new Vector2d(TILE*1.9, 2.5*TILE))
                .build();

        TrajectorySequence Left = drive.trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(-TILE*1.5, 1.45*TILE))
                .turn(Math.toRadians(-110))
                .lineToLinearHeading(new Pose2d(TILE*-1.5, 0.5*TILE, Math.toRadians(0)))
                .lineToLinearHeading(new Pose2d(TILE*1.5, 0.5*TILE, Math.toRadians(0)))
                .lineToLinearHeading(new Pose2d(TILE*2, 1.75*TILE, Math.toRadians(0)))

                .lineToConstantHeading(new Vector2d(TILE*1.9, 2.5*TILE))
                .build();



        TeamElementPipeline.MarkerPosistion markerPosistion;
        Vision.startStreaming(hardwareMap, telemetry);
        markerPosistion = TeamElementPipeline.MarkerPosistion.CENTER;
        while(opModeInInit()) {
            markerPosistion = Vision.determineMarkerPosistion();
            if(gamepad1.a)CommandScheduler.getInstance().schedule(new ClawCloseCommand(clawSubsystem));
            CommandScheduler.getInstance().run();
        }
        ////////////////////////////////////////////


        waitForStart();
        CommandScheduler.getInstance().schedule(new TiltGoToPosition(tiltSubsystem,TELEOP_INTAKE));
        Vision.webcam.stopStreaming();
        switch (markerPosistion) {
            case CENTER:
            case UNKNOWN:
                drive.followTrajectorySequenceAsync(Center);
                break;
            case RIGHT:
                drive.followTrajectorySequenceAsync(Right);
                break;
            case LEFT:
                drive.followTrajectorySequenceAsync(Left);
                break;
        }
        /*telemetry.addData("DONE 2",0);
        telemetry.update();*/
        while(!isStopRequested()) {
            drive.update();
            CommandScheduler.getInstance().run();
        }
    }

}