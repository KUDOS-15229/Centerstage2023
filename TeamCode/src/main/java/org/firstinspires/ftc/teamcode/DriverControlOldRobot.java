package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class DriverControlOldRobot extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        DcMotor extendingArm = hardwareMap.dcMotor.get("extendingArm");
        DcMotor pixelArm = hardwareMap.dcMotor.get("pixelArm");
        DcMotor intakeMotor1 = hardwareMap.dcMotor.get("intakeMotor1");
        DcMotor intakeMotor2 = hardwareMap.dcMotor.get("intakeMotor2");
        Servo klawServo = hardwareMap.servo.get("dropPixel");

        boolean capPower25 = false;

        // Reverse the right side motors. This may be wrong for your setup.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;


        while (opModeIsActive()) {

            double y = gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            telemetry.addData("pixelArmPosition", pixelArm.getCurrentPosition());
            //telemetry.update();
            telemetry.addData("extendingArmPosition", extendingArm.getCurrentPosition());
            //telemetry.update();
            //telemetry.update();
            telemetry.addData("dropPixelPosition", klawServo.getPosition());
            telemetry.update();

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;
            // Check if power should be capped
            if (capPower25) {
                // Cap the motor powers at 25%
                double maxPower = 0.5;
                frontLeftPower = Math.max(-maxPower, Math.min(maxPower, frontLeftPower));
                backLeftPower = Math.max(-maxPower, Math.min(maxPower, backLeftPower));
                frontRightPower = Math.max(-maxPower, Math.min(maxPower, frontRightPower));
                backRightPower = Math.max(-maxPower, Math.min(maxPower, backRightPower));
            }

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);


            // Check if dpad down button is pressed to cap power
            if (gamepad1.dpad_down) {
                capPower25 = true;
            } else if (gamepad1.dpad_up) {
                // If dpad up button is pressed, revert to original functionality
                capPower25 = false;
            }


            if (gamepad2.right_trigger > 0) {
                //pick up pixel
                intakeMotor1.setPower(-1);
                intakeMotor2.setPower(1);
            } else {
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);
            }
            if (gamepad2.left_trigger > 0) {
                //spit out pixel
                intakeMotor1.setPower(1);
                intakeMotor2.setPower(-1);
            } else {
                intakeMotor1.setPower(0);
                intakeMotor2.setPower(0);
            }

            if (gamepad2.right_stick_y > 0) {
                //move arm back towards front of robot
                pixelArm.setDirection(DcMotorSimple.Direction.REVERSE);
                pixelArm.setPower(.1);
            } else {
                pixelArm.setPower(0);

            }
            if (gamepad2.right_stick_y < 0) {
                //move arm towards back of robot

                pixelArm.setDirection(DcMotorSimple.Direction.FORWARD);
                pixelArm.setPower(.7);

            } else {
                pixelArm.setPower(0.2);
            }

            if (gamepad2.dpad_down) {
                //open pixel box / open
                klawServo.setPosition(klawServo.getPosition() - 0.01);
            }
            if (gamepad2.dpad_up) {
                //Close pixel box
                klawServo.setPosition(klawServo.getPosition() + 0.01);


            }
            if (gamepad2.left_bumper) {
                //Extend Hanging Arm
                extendingArm.setDirection(DcMotor.Direction.FORWARD);
                extendingArm.setPower(1);
            } else {
                extendingArm.setPower(0);
            }
            if (gamepad2.right_bumper) {
                //Retract Hanging Arm
                extendingArm.setDirection(DcMotorSimple.Direction.REVERSE);
                extendingArm.setPower(1);

            } else {
                extendingArm.setPower(0);
            }



            //slows the program down, helps keep behavior more predictable
            idle();
        }
    }
}