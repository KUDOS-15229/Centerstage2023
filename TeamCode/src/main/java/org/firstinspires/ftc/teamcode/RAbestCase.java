package org.firstinspires.ftc.teamcode;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.vision.RedDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous
public class RAbestCase extends LinearOpMode {
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
        SampleMecanumDrive drive;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        // With live preview
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        // Initialize drive variable
        drive = new SampleMecanumDrive(hardwareMap);

        // Initialize the RedDetectionPipeline
        RedDetectionPipeline redPipeline = new RedDetectionPipeline(telemetry);
        camera.setPipeline(redPipeline);
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
        String selectedRectangle = redPipeline.getSelectedRectangle();
        telemetry.addData("Selected Rectangle", redPipeline.getSelectedRectangle());
        telemetry.addData("String selectedRectangle", selectedRectangle);
        telemetry.update();
        waitForStart();

        Pose2d startingPose = new Pose2d(-36,-63.5,90);
        while (opModeIsActive()) {

            if (redPipeline.getSelectedRectangle().equals ("left")) {
                //make a trajectory that goes to the left spike mark
                sleep(1000);

                //move forward 10 inches
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(750);

                //strafe left to line up with spike mark
                drive.setMotorPowers(-.4,.4,-.4,.4);
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

                //strafe right to get away from spike mark
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(600);

                //go forward to middle of field
                drive.setMotorPowers(0.4,.4,.4,.4);
                sleep(2000);


                //turn torwards pixel board
                drive.turn(Math.toRadians(-84));

                sleep(500);

                //drive towards pixel board
                drive.setMotorPowers(0.4,.4,.4,.4);
                sleep(3650);

                //strafe right towards pixel board
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(600);
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


                //strafe left and park
                drive.setMotorPowers(-.4,.4,-.4,.4);
               sleep(700);

                drive.setMotorPowers(0,0,0,0);
                sleep(30000);
            } else if (redPipeline.getSelectedRectangle().equals("center")) {
                //make a trajectory that goes to the center spike mark

                //move forward 10 inches
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(1200);

                //strafe left to line up with spike mark
                drive.setMotorPowers(-.4,.4,-.4,.4);
                sleep(350);

                drive.setMotorPowers(0,0,0,0);

                //spin the intake motors backwards very gently to release pixel
                intakeMotor1.setPower(-0.5);
                intakeMotor2.setPower(0.5);
                sleep(750);
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);

                sleep(750);

                //strafe left
                drive.setMotorPowers(-.4,.4,-.4,.4);
                sleep(750);

                //move forward
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(950);

                drive.turn(Math.toRadians(-78));

                //drive towards pixel board
                drive.setMotorPowers(0.4,.4,.4,.4);
                sleep(4013);

                //strafe right towards pixel board
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(1513);
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

                drive.setMotorPowers(-.4,-.4,-.4,-.4);
                sleep(130);

                //strafe left and park
                drive.setMotorPowers(-.4,.4,-.4,.4);
                sleep(1300);

                drive.setMotorPowers(0,0,0,0);
                sleep(130000);

            } else if (redPipeline.getSelectedRectangle().equals("right")) {
                //make a trajectory that goes to the right spike mark
                //move forward 10 inches
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(1000);
                drive.turn(Math.toRadians(-36));

                //go forward a little bit
                drive.setMotorPowers(.4,.4,.4,.4);
                sleep(250);

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
                sleep(250);

                drive.turn(Math.toRadians(36));

                //go forward a little bit
                drive.setMotorPowers(.4,.4,.4,.4);
                sleep(1250);

                drive.turn(Math.toRadians(-79));
                //drive towards pixel board
                drive.setMotorPowers(0.4,.4,.4,.4);
                sleep(3400);

                //strafe right towards right
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(1900);

                drive.setMotorPowers(0, 0, 0, 0);
                while ((sensorDistance.getDistance(DistanceUnit.INCH)>2.9)){
                    drive.setMotorPowers(.2,.2,.2,.2);

                }
                drive.setMotorPowers(0,0,0,0);
//drop pixel on board
                //dwayneHardware.placePixelForAuton();
                pixelArm.setTargetPosition(900);
                pixelArm.setPower(-1);
                pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                sleep(1000);
                pixelArm.setPower(.1);
                extendingArm.setTargetPosition(2100);
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

drive.setMotorPowers(-.2,-.2,-.2,-.2);
sleep(300);
                //strafe left and park
                drive.setMotorPowers(-.4,.4,-.4,.4);
                sleep(1900);


                drive.setMotorPowers(0,0,0,0);
                sleep(30000);

            } else {
                telemetry.addData("Not in a Trajectory", "Something went wrong");
                sleep(30000);
            }

            drive.setMotorPowers(0,0,0,0);
            sleep(30000);


        }

    }
}