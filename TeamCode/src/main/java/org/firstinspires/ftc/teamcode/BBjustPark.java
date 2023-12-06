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
public class BBjustPark extends LinearOpMode {
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

        // Initialize a timer
        ElapsedTime timer = new ElapsedTime();

        // Initialize drive variable
        drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d(12,63.5,270);
        waitForStart();


        while (opModeIsActive()) {
            timer.reset();


                Trajectory BBJustParkTrajectory = drive.trajectoryBuilder(startPose)
                        .back(3)
                        .build();
                sleep(500);
                Pose2d pose1 = drive.getPoseEstimate();
                Trajectory BBJustParkTrajectory1 = drive.trajectoryBuilder(pose1)
                        .strafeLeft(48)
                        .build();
                sleep(500);

                drive.followTrajectory(BBJustParkTrajectory1);
                drive.followTrajectory(BBJustParkTrajectory);
                drive.setMotorPowers(0,0,0,0);

                sleep(27000);



        }

    }
}