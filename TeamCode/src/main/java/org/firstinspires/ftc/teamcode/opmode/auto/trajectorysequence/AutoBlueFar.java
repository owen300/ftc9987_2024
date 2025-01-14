package org.firstinspires.ftc.teamcode.opmode.auto.trajectorysequence;

import static org.firstinspires.ftc.teamcode.robot.commands.claw.ClawOpenCommand.Side.RIGHT;
import static org.firstinspires.ftc.teamcode.robot.commands.tilt.TiltGoToPosition.TELEOP_DEPOSIT;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.AutoMecanumDrive;
import org.firstinspires.ftc.teamcode.ftcLib_DLC.AutoUtil;
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

@Autonomous(group="auto", name="Auto_Blue_Far_side")
public class AutoBlueFar extends LinearOpMode
{
    private static final double TILE = 24.0;
    @Override
    public void runOpMode() throws InterruptedException {
        RobotContainer.initiate(hardwareMap, telemetry);
        AutoMecanumDrive drive = new AutoMecanumDrive(hardwareMap);
        DriveSubsystem driveSubsystem=new DriveSubsystem(hardwareMap);
        ClawSubsystem clawSubsystem = new ClawSubsystem(hardwareMap);
        TiltSubsystem tiltSubsystem = new TiltSubsystem(hardwareMap, telemetry);
        WristSubsystem wristSubsystem = new WristSubsystem(hardwareMap);
        ExtensionSubsystem extensionSubsystem=new ExtensionSubsystem(hardwareMap,telemetry);
        Pose2d startPose =  new Pose2d(-TILE*1.6, 2.5*TILE, Math.toRadians(-90));
        drive.setPoseEstimate(startPose);

        StowEverything stow = new StowEverything(tiltSubsystem, extensionSubsystem, clawSubsystem, wristSubsystem);

        SequentialCommandGroup place_pixel_and_stow = new SequentialCommandGroup(
                new InstantCommand(()->CommandScheduler.getInstance().cancel(CommandScheduler.getInstance().requiring(clawSubsystem))),
                new TiltGoToPosition(tiltSubsystem, TiltGoToPosition.TELEOP_INTAKE),
                new WristIntake(wristSubsystem),
                new ClawOpenCommand(clawSubsystem, RIGHT).withTimeout(6000),
                new WristStow(wristSubsystem));

        SequentialCommandGroup deposit = new SequentialCommandGroup(
                new TiltGoToPosition(tiltSubsystem, TELEOP_DEPOSIT),
                new WristDeposit(wristSubsystem),
                new ClawOpenCommand(clawSubsystem, ClawOpenCommand.Side.BOTH).withTimeout(500));


        TrajectorySequence Center = drive.trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(-TILE*1.5, 1.457*TILE))
                .addTemporalMarker(1.5, () -> {
                    CommandScheduler.getInstance().schedule(place_pixel_and_stow);
                })
                .lineToLinearHeading(new Pose2d(TILE*-1.5, 0.515*TILE, Math.toRadians(-90)))
                .waitSeconds(3)
                .lineToLinearHeading(new Pose2d(TILE*-1.5, (0.515*TILE)-3, Math.toRadians(-90)))
                .turn(Math.toRadians(90))
                .waitSeconds(9)
                .lineToLinearHeading(new Pose2d(TILE*1.5, (0.5*TILE)-1, Math.toRadians(0)))
                .addDisplacementMarker(() -> {
                    CommandScheduler.getInstance().schedule(deposit);
                })
                .lineToLinearHeading(new Pose2d((TILE*2.245)+1.25, 1.7*TILE, Math.toRadians(0)))
                .lineToConstantHeading(new Vector2d(TILE*1.8, 0.5*TILE))
                .addDisplacementMarker(() -> {
                    CommandScheduler.getInstance().schedule(new StowEverything(tiltSubsystem,extensionSubsystem,clawSubsystem,wristSubsystem));
                })
                .lineToConstantHeading(new Vector2d(TILE*1.8, 0.499*TILE))
                .build();
        /////////////////


        TrajectorySequence Left = drive.trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(-TILE*1.5, 1.45*TILE))
                .turn(Math.toRadians(-95))
                .waitSeconds(2)
                .turn(Math.toRadians(95))
                .addTemporalMarker(1.4, () -> {
                    CommandScheduler.getInstance().schedule(place_pixel_and_stow);
                })
                .lineToLinearHeading(new Pose2d(TILE*-1.5, (0.5*TILE)-3, Math.toRadians(0)))
                .waitSeconds(13)
                .lineToLinearHeading(new Pose2d(TILE*1.5, (0.5*TILE)-3, Math.toRadians(0)))
                .addDisplacementMarker(() -> {
                    CommandScheduler.getInstance().schedule(deposit);
                })
                .lineToLinearHeading(new Pose2d((TILE*2.242)+1.25, (1.9*TILE)+3, Math.toRadians(0)))
                .lineToConstantHeading(new Vector2d(TILE*1.8, 0.5*TILE))
                .addDisplacementMarker(() -> {
                    CommandScheduler.getInstance().schedule(new StowEverything(tiltSubsystem,extensionSubsystem,clawSubsystem,wristSubsystem));
                })
                .lineToConstantHeading(new Vector2d(TILE*1.8, 0.499*TILE))
                .build();

        TrajectorySequence Right = drive.trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(-TILE*1.44, 1.45*TILE))
                .turn(Math.toRadians(110))
                .waitSeconds(2)
                .turn(Math.toRadians(-110))
                .addTemporalMarker(1.5, () -> {
                    CommandScheduler.getInstance().schedule(place_pixel_and_stow);
                })
                .lineToLinearHeading(new Pose2d(TILE*-1.5, (0.5*TILE)-3, Math.toRadians(0)))
                .waitSeconds(13)
                .lineToLinearHeading(new Pose2d(TILE*1.5, (0.5*TILE)-3, Math.toRadians(0)))
                .addDisplacementMarker(() -> {
                    CommandScheduler.getInstance().schedule(deposit);
                })
                .lineToLinearHeading(new Pose2d((TILE*2.245)+1.25, 1.25*TILE, Math.toRadians(0)))
                .lineToConstantHeading(new Vector2d(TILE*1.8, (0.5*TILE)-1))
                .addDisplacementMarker(() -> {
                    CommandScheduler.getInstance().schedule(new StowEverything(tiltSubsystem,extensionSubsystem,clawSubsystem,wristSubsystem));
                })
                .lineToConstantHeading(new Vector2d(TILE*1.8, 0.499*TILE))
                .build();



        TeamElementPipeline.MarkerPosistion markerPosistion;
        Vision.startStreaming(hardwareMap, telemetry);
        markerPosistion = TeamElementPipeline.MarkerPosistion.CENTER;
        while(opModeInInit()) {
            markerPosistion = Vision.determineMarkerPosistion();
            if(gamepad1.a) {
                driveSubsystem.init();
                tiltSubsystem.init();
                extensionSubsystem.init();
                CommandScheduler.getInstance().schedule(new ClawCloseCommand(clawSubsystem));
                CommandScheduler.getInstance().schedule(new StowEverything(tiltSubsystem,extensionSubsystem,clawSubsystem,wristSubsystem));
            }
            CommandScheduler.getInstance().run();
        }
        ////////////////////////////////////////////


        waitForStart();
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