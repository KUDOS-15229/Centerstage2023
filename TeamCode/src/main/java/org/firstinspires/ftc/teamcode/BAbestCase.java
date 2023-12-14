package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.vision.BlueDetectionPipeline;
import org.firstinspires.ftc.vision.VisionPortal;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class BAbestCase extends LinearOpMode {
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
        DistanceSensor sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_distance");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        // With live preview
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);


        SampleMecanumDrive drive;

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

        telemetry.addData("Selected Rectangle", bluePipeline.getSelectedRectangle());
        telemetry.update();

        //this tells the program where we are starting on the field
        //and where which direction we are facing
        Pose2d startingPose = new Pose2d(-36, 63.5, Math.toRadians(270));

        waitForStart();


        while (opModeIsActive()) {
            String selectedRectangle = bluePipeline.getSelectedRectangle();
            telemetry.addData("String selectedRectangle", selectedRectangle);
            telemetry.update();

            if (bluePipeline.getSelectedRectangle().equals("left")){
                //if on a team with KRASH add sleep.
               // sleep(3000);
                //move forward 10 inches
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(1000);
                drive.turn(Math.toRadians(42));

                //go forward a little bit
                drive.setMotorPowers(.4,.4,.4,.4);
                sleep(275);

                drive.setMotorPowers(0,0,0,0);

                //spin the intake motors backwards very gently to release pixel
                intakeMotor1.setPower(-0.5);
                intakeMotor2.setPower(0.5);
                sleep(750);
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);

                sleep(750);

                //back up a little
                drive.setMotorPowers(-.4,-.4,-.4,-.4);
                sleep(275);

                drive.turn(Math.toRadians(-42));

                //go forward a little bit
                drive.setMotorPowers(.4,.4,.4,.4);
                sleep(1350);

                drive.turn(Math.toRadians(85));
                //drive towards pixel board
                drive.setMotorPowers(0.4,.4,.4,.4);
                sleep(3000);
                //strafe left
                drive.setMotorPowers(-.4,.4,-.4,.4);
                sleep(2400);
                drive.setMotorPowers(0,0,0,0);
                while ((sensorDistance.getDistance(DistanceUnit.INCH)>2.9)){
                    drive.setMotorPowers(.2,.2,.2,.2);

                }
                drive.setMotorPowers(0,0,0,0);

                //drop pixel on board
                //dwayneHardware.placePixelForAuton();
                pixelArm.setTargetPosition(845);
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
                dropPixel.setPosition(dropPixel.getPosition()+.5);
                extendingArm.setTargetPosition(0);
                extendingArm.setPower(-1);
                extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                pixelArm.setTargetPosition(0);
                pixelArm.setPower(.4);
                pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);

                //strafe right
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(1800);
                drive.setMotorPowers(0,0,0,0);

                sleep(30000000);


            } else if (bluePipeline.getSelectedRectangle().equals("center")) {
                //make a trajectory that goes to the center spike mark
                //move forward 10 inches
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(1200);

                //strafe right to line up with spike mark
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(350);

                drive.setMotorPowers(0,0,0,0);

                //spin the intake motors backwards very gently to release pixel
                intakeMotor1.setPower(-0.5);
                intakeMotor2.setPower(0.5);
                sleep(750);
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);

                sleep(650);

                //move backward
                drive.setMotorPowers(-0.4,-0.4,-0.4,-0.4);
                sleep(113);

                //strafe right
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(750);

                //move forward
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(1213);

                drive.turn(Math.toRadians(84));

                //drive towards pixel board
                drive.setMotorPowers(0.4,.4,.4,.4);
                sleep(4500);
                //strafe left
                drive.setMotorPowers(-.4,.4,-.4,.4);
                sleep(1700);
                drive.setMotorPowers(0,0,0,0);

                //drop pixel on board
                //dwayneHardware.placePixelForAuton();
                pixelArm.setTargetPosition(900);
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
                dropPixel.setPosition(dropPixel.getPosition()+.5);
                extendingArm.setTargetPosition(0);
                extendingArm.setPower(-1);
                extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                pixelArm.setTargetPosition(0);
                pixelArm.setPower(.4);
                pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);

                //strafe left
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(1400);
                drive.setMotorPowers(0,0,0,0);
                sleep(30000);

            } else if (bluePipeline.getSelectedRectangle().equals("right")){
                //make a trajectory that goes to the right spike mark
                //move forward 10 inches
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(775);

                //strafe right to line up with spike mark
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(750);

                drive.setMotorPowers(0,0,0,0);

                //spin the intake motors backwards very gently to release pixel
                intakeMotor1.setPower(-0.5);
                intakeMotor2.setPower(0.5);
                sleep(750);
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);

                sleep(750);

                //back up a little bit
                drive.setMotorPowers(-.2,-.2,-.2,-.2);
                sleep(750);

                //strafe left to get away from spike mark
                drive.setMotorPowers(-.4,.4,-.4,.4);
                sleep(600);

                //go forward to middle of field
                drive.setMotorPowers(0.4,.4,.4,.4);
                sleep(1750);


                //turn torwards pixel board
                drive.turn(Math.toRadians(84));

                sleep(500);

                //drive towards pixel board
                drive.setMotorPowers(0.4,.4,.4,.4);
                sleep(3700);

                //strafe left
                drive.setMotorPowers(-.4,.4,-.4,.4);
                sleep(500);
                drive.setMotorPowers(0,0,0,0);

                //drop pixel on board
                //dwayneHardware.placePixelForAuton();
                pixelArm.setTargetPosition(900);
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
                dropPixel.setPosition(dropPixel.getPosition()+.5);
                extendingArm.setTargetPosition(0);
                extendingArm.setPower(-1);
                extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                pixelArm.setTargetPosition(0);
                pixelArm.setPower(.4);
                pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);

                //strafe right
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(1000);

                drive.setMotorPowers(0,0,0,0);
                sleep(30000);

            } else {
                telemetry.addData("Not in a Trajectory", "Something went wrong");
                sleep(30000);
            }

            //Stop the motors
            drive.setMotorPowers(0,0,0,0);
            sleep(30000);


        }

    }
}