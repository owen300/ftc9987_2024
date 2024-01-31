package org.firstinspires.ftc.teamcode.opmode.auto;

import static org.firstinspires.ftc.teamcode.robot.commands.tilt.TiltGoToPosition.TELEOP_DEPOSIT;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.AutoMecanumDrive;
import org.firstinspires.ftc.teamcode.ftcLib_DLC.AutoUtil;
import org.firstinspires.ftc.teamcode.opmode.auto.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.robot.commands.claw.ClawCloseCommand;
import org.firstinspires.ftc.teamcode.robot.commands.claw.ClawOpenCommand;
import org.firstinspires.ftc.teamcode.robot.commands.tilt.TiltGoToPosition;
import org.firstinspires.ftc.teamcode.robot.commands.wrist.WristDeposit;
import org.firstinspires.ftc.teamcode.robot.commands.wrist.WristIntake;
import org.firstinspires.ftc.teamcode.robot.commands.wrist.WristStow;
import org.firstinspires.ftc.teamcode.robot.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.robot.subsystems.TiltSubsystem;
import org.firstinspires.ftc.teamcode.robot.subsystems.WristSubsystem;
import org.firstinspires.ftc.teamcode.vision.TeamElementPipeline;
import org.firstinspires.ftc.teamcode.vision.Vision;

@Autonomous(group="auto", name="Auto_BLUE_Backboard_side")
public class AutoBlueLeft extends LinearOpMode
{
    private static final double TILE = 24.0;
    @Override
    public void runOpMode() throws InterruptedException
    {
        AutoMecanumDrive drive = new AutoMecanumDrive(hardwareMap);

        ClawSubsystem clawSubsystem = new ClawSubsystem(hardwareMap);
        TiltSubsystem tiltSubsystem = new TiltSubsystem(hardwareMap, telemetry);
        WristSubsystem wristSubsystem = new WristSubsystem(hardwareMap);

        Pose2d startPose = new Pose2d(TILE - 18, 2.5 * TILE, Math.toRadians(-90));

        drive.setPoseEstimate(startPose);

        ParallelCommandGroup initiate = new ParallelCommandGroup(
                new ClawCloseCommand(clawSubsystem),
                new WristStow(wristSubsystem),
                new TiltGoToPosition(tiltSubsystem, TiltGoToPosition.TELEOP_INTAKE)
        );
        SequentialCommandGroup place_pixel_and_stow = new SequentialCommandGroup(
                new TiltGoToPosition(tiltSubsystem, TiltGoToPosition.AUTO_STACK_INTAKE1),
                new WristIntake(wristSubsystem),
                new ClawOpenCommand(clawSubsystem, ClawOpenCommand.Side.RIGHT),
                new WristStow(wristSubsystem),
                new TiltGoToPosition(tiltSubsystem, TiltGoToPosition.TELEOP_INTAKE));

        SequentialCommandGroup deposit = new SequentialCommandGroup(
                new TiltGoToPosition(tiltSubsystem, TELEOP_DEPOSIT),
                new WristDeposit(wristSubsystem),
                new ClawOpenCommand(clawSubsystem, ClawOpenCommand.Side.RIGHT));


        TrajectorySequence Center = drive.trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(TILE / 2, 1.53 * TILE + 1))
                //.lineToConstantHeading(new Vector2d(TILE/2, -1.5*TILE))
                .turn(Math.toRadians(170))
                .addTemporalMarker(1.3, () -> {
                    telemetry.addData("RUNNING BEFORE", 0);
                    telemetry.update();
                    CommandScheduler.getInstance().schedule(place_pixel_and_stow);
                    CommandScheduler.getInstance().run();
                    telemetry.addData("RUNNING AFTER", 0);
                    telemetry.update();
                })
                .waitSeconds(5)
                .turn(Math.toRadians(170 - 90 - 180 + 30))
                .addDisplacementMarker(() -> {
                    CommandScheduler.getInstance().schedule(deposit);
                })
                .waitSeconds(5)
                .lineToConstantHeading(new Vector2d(2 * TILE, 37))
                .waitSeconds(2)
                .lineToConstantHeading(new Vector2d(2 * TILE, 2.5 * TILE))
                .build();

        TrajectorySequence Right = drive.trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(TILE/2, 1.3*TILE))
                .turn(Math.toRadians(90))
                .lineToConstantHeading(new Vector2d((TILE*0.44)+1, 1.29*TILE))
                .addTemporalMarker(1.3, () -> {
                    telemetry.addData("RUNNING BEFORE", 0);
                    telemetry.update();
                    CommandScheduler.getInstance().schedule(place_pixel_and_stow);
                    CommandScheduler.getInstance().run();
                    telemetry.addData("RUNNING AFTER", 0);
                    telemetry.update();
                }).build();

        TrajectorySequence Left = drive.trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(TILE/2, 1.3*TILE))
                .turn(Math.toRadians(90))
                .lineToConstantHeading(new Vector2d((TILE*1.43)-0.3, 1.3*TILE))
                .addTemporalMarker(2.2+1, () -> {
                    telemetry.addData("RUNNING BEFORE", 0);
                    telemetry.update();
                    CommandScheduler.getInstance().schedule(place_pixel_and_stow);
                    CommandScheduler.getInstance().run();
                    telemetry.addData("RUNNING AFTER", 0);
                    telemetry.update();
                })
                .waitSeconds(6)
                .lineToConstantHeading(new Vector2d(TILE*1.7, 1.8*TILE))
                .addDisplacementMarker(()->{
                    CommandScheduler.getInstance().schedule(deposit);
                })
                .waitSeconds(3)
                .lineToConstantHeading(new Vector2d(2*TILE, 33*TILE))
                .waitSeconds(3)
                .lineToConstantHeading(new Vector2d(2*TILE, 2.5*TILE))
                .build();

        TeamElementPipeline.MarkerPosistion markerPosistion;
        Vision.startStreaming(hardwareMap, telemetry);
        markerPosistion = TeamElementPipeline.MarkerPosistion.CENTER;
        CommandScheduler.getInstance().schedule(initiate);
        CommandScheduler.getInstance().run();
        while(opModeInInit()) {
            markerPosistion = Vision.determineMarkerPosistion();
        }
        ////////////////////////////////////////////


        waitForStart();
        Vision.webcam.stopStreaming();
        switch (markerPosistion) {
            case UNKNOWN:
                drive.followTrajectorySequenceAsync(Center);
                break;
            case CENTER:
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
