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
public class RBbestCase extends LinearOpMode {
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
            }
        });


        // Access the selected rectangle using redPipeline.getSelectedRectangle()
        String selectedRectangle = redPipeline.getSelectedRectangle();
        telemetry.addData("Selected Rectangle", redPipeline.getSelectedRectangle());
        telemetry.addData("String selectedRectangle", selectedRectangle);
        telemetry.update();
        waitForStart();

        Pose2d startingPose= new Pose2d(12, -63.5, Math.toRadians(90));
        while (opModeIsActive()) {

            if (redPipeline.getSelectedRectangle().equals("left")) {

                //move forward 10 inches
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(1100);
                drive.turn(Math.toRadians(45));

                //go forward a little bit
                drive.setMotorPowers(.4,.4,.4,.4);
                sleep(225);

                drive.setMotorPowers(0,0,0,0);

                //spin the intake motors backwards very gently to release pixel
                intakeMotor1.setPower(-0.5);
                intakeMotor2.setPower(0.5);
                sleep(750);
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);

                sleep(750);

                //go back a little bit
                drive.setMotorPowers(-.4,-.4,-.4,-.4);
                sleep(275);

                drive.turn(Math.toRadians(-130));
                sleep(500);

                //forward to board and strafe left
//                  drive.setMotorPowers(.4, .4, .4, .4);
//                  sleep(1300);
                while ((sensorDistance.getDistance(DistanceUnit.INCH)>2.9)){
                    drive.setMotorPowers(.2,.2,.2,.2);
                }
                drive.setMotorPowers(0,0,0,0);
                  drive.setMotorPowers(-.4,.4,-.4,.4);
                  sleep(750);
                  drive.setMotorPowers(0,0,0,0);

//              place pixel
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

                drive.setMotorPowers(.4,-.4,.4,-.4);
               sleep(1600);
 drive.setMotorPowers(0,0,0,0);
                //back up a little bit more
//                drive.setMotorPowers(-.4,-.4,-.4,-.4);
//                sleep(850);
//
//                //strafe right to park
//                drive.setMotorPowers(.4,-.4,.4,-.4);
//                sleep(2500);
//                drive.setMotorPowers(0,0,0,0);
                sleep(30000);


            } else if (redPipeline.getSelectedRectangle().equals("center")) {
                //make a trajectory that goes to the center spike mark
                //move forward 10 inches
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(1185);

                //strafe right to line up with spike mark
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(200);

                drive.setMotorPowers(0,0,0,0);

                //spin the intake motors backwards very gently to release pixel
                intakeMotor1.setPower(-0.5);
                intakeMotor2.setPower(0.5);
                sleep(750);
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);

                sleep(750);
                //move back 10 inches
                drive.setMotorPowers(-0.4,-0.4,-0.4,-0.4);
                sleep(250);

                //turn right towards board
                drive.turn(Math.toRadians(-83));

                //drive forward towards board

                while ((sensorDistance.getDistance(DistanceUnit.INCH)>3.1)){
                    drive.setMotorPowers(.2,.2,.2,.2);
                }
                drive.setMotorPowers(0,0,0,0);

//                drive.setMotorPowers(.4,.4,.4,.4);
//                sleep(1600);

                //strafe left towards center
                drive.setMotorPowers(-.4,.4,-.4,.4);
                sleep(530);

                //stop
                drive.setMotorPowers(0,0,0,0);

                //drop pixel on board
                pixelArm.setTargetPosition(850);
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


                //strafe right to park
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(2500);
                drive.setMotorPowers(0,0,0,0);
                sleep(30000);



            } else if (redPipeline.getSelectedRectangle().equals("right")) {
                //if selectedRectangle isn't left or center, it's "right"

                //make a trajectory that goes to the right spike mark
                //move forward 10 inches
                drive.setMotorPowers(0.4,0.4,0.4,0.4);
                sleep(750);

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
                drive.setMotorPowers(-.4,-.4,-.4,-.4);
                sleep(75);

                //turn right towards board
                drive.turn(Math.toRadians(-85));

//                //drive forward towards board

                while ((sensorDistance.getDistance(DistanceUnit.INCH)>2.9)){
                    drive.setMotorPowers(.2,.2,.2,.2);
                }
                drive.setMotorPowers(0,0,0,0);

//                drive.setMotorPowers(.4,.4,.4,.4);
//                sleep(1450);
//                drive.setMotorPowers(0,0,0,0);

                //strafe left towards center
                drive.setMotorPowers(-.4,.4,-.4,.4);
                sleep(650);
                drive.setMotorPowers(0,0,0,0);

                //drop pixel on board
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


                //strafe right to park
                drive.setMotorPowers(.4,-.4,.4,-.4);
                sleep(2000);
                drive.setMotorPowers(0,0,0,0);
                sleep(30000);


                ;

                //Stop the motors
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