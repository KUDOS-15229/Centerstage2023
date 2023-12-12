package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class AirplaneServoTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        DcMotor extendingArm = hardwareMap.dcMotor.get("extendingArm");
        DcMotor pixelArm = hardwareMap.dcMotor.get("pixelArm");
        DcMotor intakeMotor1 = hardwareMap.dcMotor.get("intakeMotor1");
        DcMotor intakeMotor2 = hardwareMap.dcMotor.get("intakeMotor2");
        Servo dropPixel = hardwareMap.servo.get("dropPixel");
        Servo airplaneServo = hardwareMap.servo.get("airplaneServo");
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (gamepad2.dpad_right && gamepad2.b) {
                //airplane! :) (bring back to top)
                airplaneServo.setDirection(Servo.Direction.FORWARD);
                airplaneServo.setPosition(0.8);
                telemetry.addData("Forward...Servo Position is set to:", airplaneServo.getPosition()) ;
                telemetry.update();
            }
            if (gamepad2.dpad_left && gamepad2.b) {
                //airplane! :) (shoot!)
                airplaneServo.setDirection(Servo.Direction.REVERSE);
                airplaneServo.setPosition(0.6);
                telemetry.addData("Reverse...Servo Position is set to:", airplaneServo.getPosition()) ;
                telemetry.update();
            }

        }


    }
}
