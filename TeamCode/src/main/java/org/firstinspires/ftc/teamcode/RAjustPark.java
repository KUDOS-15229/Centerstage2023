package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
@Disabled
@Autonomous
public class RAjustPark extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware, motors, servos etc
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        DcMotor extendingArm = hardwareMap.dcMotor.get("extendingArm");
        DcMotor pixelArm = hardwareMap.dcMotor.get(("pixelArm"));
        DcMotor intakeMotor1 = hardwareMap.dcMotor.get("intakeMotor1");
        DcMotor intakeMotor2 = hardwareMap.dcMotor.get("intakeMotor2");
        Servo airplaneServo = hardwareMap.servo.get("airplaneServo");
        Servo dropPixel = hardwareMap.servo.get("dropPixel");
        SampleMecanumDrive drive;

        // Initialize drive variable
        drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d(-36,-63.5,90);
        waitForStart();


        while (opModeIsActive()) {

            drive.setMotorPowers(0.2,.2,.2,.2);
            sleep(500);

            drive.setMotorPowers(-0.5,.5,-.5,.5);
            sleep(900);

            drive.setMotorPowers(0.6,.6,.6,.6);
            sleep(875);

            sleep(500);

            drive.turn(Math.toRadians(-80));

            drive.setMotorPowers(0.4,.4,.4,.4);
            sleep(4800);

//            Trajectory RAJustParkTrajectory = drive.trajectoryBuilder(startPose)
//                    .forward(2)
//                    .build();
//            sleep(500);
//
//            Pose2d pose1 = drive.getPoseEstimate();
//            Trajectory RAJustParkTrajectory1 = drive.trajectoryBuilder(pose1)
//                    .strafeLeft(2)
//                    .build();
//            sleep(500);
//
//            Pose2d pose2 = drive.getPoseEstimate();
//            Trajectory RAJustParkTrajectory2 = drive.trajectoryBuilder(pose2)
//                    .forward(32)
//                    .build();
//            sleep(500);
//
//            Pose2d pose3 = drive.getPoseEstimate();
//            Trajectory RAJustParkTrajectory3 = drive.trajectoryBuilder(pose3)
//                    .strafeRight(120)
//                    .build();
//            sleep(500);
            drive.setMotorPowers(0,0,0,0);

//            drive.followTrajectory(RAJustParkTrajectory);
//            drive.followTrajectory(RAJustParkTrajectory1);
//            drive.followTrajectory(RAJustParkTrajectory2);
//            drive.followTrajectory(RAJustParkTrajectory3);
            sleep(27000);
        }

    }
}