package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
@Disabled
@Autonomous
public class BAjustPark extends LinearOpMode {
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

        // Create a starting pose
        Pose2d startPose = new Pose2d(-36,63.5,270);
        waitForStart();


        while (opModeIsActive()) {

            Trajectory BAJustParkTrajectory = drive.trajectoryBuilder(startPose)
                    .forward(2)
                    .build();
            sleep(500);
            Pose2d pose1 = drive.getPoseEstimate();
            Trajectory BAJustParkTrajectory1 = drive.trajectoryBuilder(pose1)
                    .strafeRight(12)
                    .build();
            sleep(500);
            Pose2d pose2 = drive.getPoseEstimate();
            Trajectory BAJustParkTrajectory2 = drive.trajectoryBuilder(pose2)
                    .forward(32)
                    .build();
            sleep(500);
            Pose2d pose3 = drive.getPoseEstimate();
            Trajectory BAJustParkTrajectory3 = drive.trajectoryBuilder(pose3)
                    .strafeLeft(120)
                    .build();
            sleep(500);
            drive.setMotorPowers(0,0,0,0);

            drive.followTrajectory(BAJustParkTrajectory);
            drive.followTrajectory(BAJustParkTrajectory1);
            drive.followTrajectory(BAJustParkTrajectory2);
            drive.followTrajectory(BAJustParkTrajectory3);
            //make sure robot doesn't move
            sleep(27000);


        }

    }
}