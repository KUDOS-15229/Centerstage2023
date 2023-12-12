package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.drive.DriveConstants.encoderTicksToInches;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.vision.BlueDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
import java.util.List;
@Disabled
@Autonomous
public class OdemetryTest

extends LinearOpMode {
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
//        DwayneHardware dwayneHardware = null;
//
//        dwayneHardware.init();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        // With live preview
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        // Initialize drive variable
        drive = new SampleMecanumDrive(hardwareMap);

        // Initialize the BlueDetectionPipeline
        BlueDetectionPipeline bluePipeline = new BlueDetectionPipeline(telemetry);
        camera.setPipeline(bluePipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // Configure camera settings if needed
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPSIDE_DOWN);
            }

            @Override
            public void onError(int errorCode) {
                // Handle the error based on the errorCode
                telemetry.addData("Error", "Camera failed to open with error code: " + errorCode);
                telemetry.update();
                System.out.println("Webcam error code: " + errorCode);
            }
        });


        // Access the selected rectangle using redPipeline.getSelectedRectangle()
        String selectedRectangle = bluePipeline.getSelectedRectangle();
        telemetry.addData("Selected Rectangle", bluePipeline.getSelectedRectangle());
        telemetry.addData("String selectedRectangle", selectedRectangle);
        telemetry.update();
        Pose2d startingPose = new Pose2d(12, 63.5, Math.toRadians(270));
        ArrayList<DcMotor> motors = new ArrayList<>();
        motors.add(frontLeftMotor);
        motors.add(frontRightMotor);
        motors.add(backLeftMotor);
        motors.add(backRightMotor);
        waitForStart();


        while (opModeIsActive()) {
            List<Double> wheelPositions = new ArrayList<>();

            for (DcMotor motor : motors) {
                double position = motor.getCurrentPosition();
//              wheelPositions.add(position);
                wheelPositions.add(encoderTicksToInches(position));
                telemetry.addData("motor: " + motor.getDeviceName(), encoderTicksToInches(position));
            }
            //prevent program from going into a trajectory while we're testing the above code
        sleep(30000);

            if (bluePipeline.getSelectedRectangle().equals("left")) {
                //make a trajectory that goes to the left spike mark

                //move forward 10 inches
                drive.setMotorPowers(0.4, 0.4, 0.4, 0.4);
                sleep(775);

                //strafe left to line up with spike mark
                drive.setMotorPowers(-.4, .4, -.4, .4);
                sleep(800);

                drive.setMotorPowers(0, 0, 0, 0);

                //spin the intake motors backwards very gently to release pixel
                //dwayneHardware.spitOut();
                intakeMotor1.setPower(-0.5);
                intakeMotor2.setPower(0.5);
                sleep(750);
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);

                sleep(750);

                //back up a little bit
                drive.setMotorPowers(-.2, -.2, -.2, -.2);
                sleep(750);

                //turn towards board
                drive.turn(Math.toRadians(87));

                //drive forward towards board
                drive.setMotorPowers(.4, .4, .4, .4);
                sleep(1100);

                //strafe right to line up with left mark
                drive.setMotorPowers(.4, -.4, .4, -.4);
                sleep(800);

                drive.setMotorPowers(0, 0, 0, 0);

                //drop pixel on board
                // dwayneHardware.placePixelForAuton();
                pixelArm.setTargetPosition(810);
                pixelArm.setPower(-1);
                pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                pixelArm.setPower(.1);
                extendingArm.setTargetPosition(2000);
                extendingArm.setPower(1);
                extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                dropPixel.setPosition(.5);
                sleep(1000);
                dropPixel.setPosition(dropPixel.getPosition() + .5);
                extendingArm.setTargetPosition(0);
                extendingArm.setPower(-1);
                extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                pixelArm.setTargetPosition(0);
                pixelArm.setPower(.4);
                pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);


                //strafe left to park
                drive.setMotorPowers(-.4, .4, -.4, .4);
                sleep(2500);
                drive.setMotorPowers(0, 0, 0, 0);
                sleep(30000);


            } else if (bluePipeline.getSelectedRectangle().equals("center")) {
                //make a trajectory that goes to the center spike mark
                //move forward 10 inches
                drive.setMotorPowers(0.4, 0.4, 0.4, 0.4);
                sleep(1050);

                //strafe left to line up with spike mark
                drive.setMotorPowers(-.4, .4, -.4, .4);
                sleep(450);

                drive.setMotorPowers(0, 0, 0, 0);

                //spin the intake motors backwards very gently to release pixel
                intakeMotor1.setPower(-0.5);
                intakeMotor2.setPower(0.5);
                sleep(750);
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);
//dwayneHardware.spitOut();

                sleep(750);
                //move back 10 inches
                drive.setMotorPowers(-0.4, -0.4, -0.4, -0.4);
                sleep(250);

                //turn left towards board
                drive.turn(Math.toRadians(85));

                //drive forward towards board
                drive.setMotorPowers(.4, .4, .4, .4);
                sleep(1315);

                //strafe Right towards center;
                drive.setMotorPowers(.4, -.4, .4, -.4);
                sleep(650);

                //stop
                drive.setMotorPowers(0, 0, 0, 0);
//                  place pixels on board
                //  dwayneHardware.placePixelForAuton();
                pixelArm.setTargetPosition(810);
                pixelArm.setPower(-1);
                pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                pixelArm.setPower(.1);
                extendingArm.setTargetPosition(2000);
                extendingArm.setPower(1);
                extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                dropPixel.setPosition(.5);
                sleep(1000);
                dropPixel.setPosition(dropPixel.getPosition() + .5);
                extendingArm.setTargetPosition(0);
                extendingArm.setPower(-1);
                extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                pixelArm.setTargetPosition(0);
                pixelArm.setPower(.4);
                pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);


                //strafe left to park
                drive.setMotorPowers(-.4, .4, -.4, .4);
                sleep(2500);
                drive.setMotorPowers(0, 0, 0, 0);
                sleep(30000);


            } else if (bluePipeline.getSelectedRectangle().equals("right")) {

                //move forward 10 inches
                drive.setMotorPowers(0.4, 0.4, 0.4, 0.4);
                sleep(850);
                drive.turn(Math.toRadians(-42));

                //go forward a little bit
                drive.setMotorPowers(.4, .4, .4, .4);
                sleep(260);

                drive.setMotorPowers(0, 0, 0, 0);

                //spin the intake motors backwards very gently to release pixel
                intakeMotor1.setPower(-0.5);
                intakeMotor2.setPower(0.5);
                sleep(750);
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);
                //dwayneHardware.spitOut();

                sleep(750);

                //go back a little bit
                drive.setMotorPowers(-.4, -.4, -.4, -.4);
                sleep(275);

                drive.turn(Math.toRadians(130));

                // Go forward to pixel board
                drive.setMotorPowers(.4, .4, .4, .4);
                sleep(1590);

                //strafe right
                drive.setMotorPowers(.4, -.4, .4, -.4);
                sleep(1000);
                drive.setMotorPowers(0, 0, 0, 0);


                //drop pixel on board
                //dwayneHardware.placePixelForAuton();
                pixelArm.setTargetPosition(810);
                pixelArm.setPower(-1);
                pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                pixelArm.setPower(.1);
                extendingArm.setTargetPosition(2000);
                extendingArm.setPower(1);
                extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                dropPixel.setPosition(.5);
                sleep(1000);
                dropPixel.setPosition(dropPixel.getPosition() + .5);
                extendingArm.setTargetPosition(0);
                extendingArm.setPower(-1);
                extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                pixelArm.setTargetPosition(0);
                pixelArm.setPower(.4);
                pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);


                //strafe left to park
                drive.setMotorPowers(-.4, .4, -.4, .4);
                sleep(2000);
                drive.setMotorPowers(0, 0, 0, 0);
                sleep(30000);


                sleep(30000);
            } else {
                telemetry.addData("Not in a Trajectory", "Something went wrong");
                sleep(30000);
            }

            //Stop the motors
            drive.setMotorPowers(0, 0, 0, 0);
            sleep(30000);
        }
    }
}
