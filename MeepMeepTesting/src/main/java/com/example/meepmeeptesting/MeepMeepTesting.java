package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    private static final double TILE = 24.0;
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        /*
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep) // path for red side middle pixel
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 14)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(TILE/2, -2.5*TILE, Math.toRadians(90)))
                                .lineToConstantHeading(new Vector2d(TILE/2, -1.5*TILE))
                                //.lineToConstantHeading(new Vector2d(TILE/2, -1.5*TILE))
                                .turn(Math.toRadians(170))
                                .addTemporalMarker(1, () -> {

                                })
                                .turn(Math.toRadians(-170+90))
                                .lineToConstantHeading(new Vector2d(2*TILE, -1.5*TILE))
                                .addTemporalMarker(1, () -> {

                                })
                                .lineToConstantHeading(new Vector2d(2*TILE, -3*TILE))
                                .build()
                );*/

        /*RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep) // path for red side right pixel
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 14)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(TILE/2, -2.5*TILE, Math.toRadians(90)))
                                .lineToConstantHeading(new Vector2d(TILE/2, -1*TILE))
                                .turn(Math.toRadians(45))
                                .addTemporalMarker(1, () -> {

                                })
                                .turn(Math.toRadians(45))
                                .lineToConstantHeading(new Vector2d(TILE/2, -2*TILE))

                                .lineToConstantHeading(new Vector2d(2*TILE, -1.75*TILE))
                                .addTemporalMarker(1, () -> {

                                })
                                .lineToConstantHeading(new Vector2d(2*TILE, -2.5*TILE))
                                .build()
                );*/

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep) // path for red side left pixel
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 14)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder( new Pose2d(TILE*0.4, 2.5*TILE, Math.toRadians(-90)))

                                .lineToConstantHeading(new Vector2d(-TILE*1.5, 1.45*TILE))
                                .turn(Math.toRadians(-110))
                                .lineToLinearHeading(new Pose2d(TILE*-1.5, 0.5*TILE, Math.toRadians(0)))
                                .lineToLinearHeading(new Pose2d(TILE*1.5, 0.5*TILE, Math.toRadians(0)))
                                .lineToLinearHeading(new Pose2d(TILE*2, 1.75*TILE, Math.toRadians(0)))

                                .lineToConstantHeading(new Vector2d(TILE*1.9, 2.5*TILE))

                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}