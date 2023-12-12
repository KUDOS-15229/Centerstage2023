package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@Autonomous
public class PixelArmTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
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

        pixelArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        extendingArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
waitForStart();
        while (opModeIsActive()) {




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
            dropPixel.setPosition(dropPixel.getPosition()+.5);
            extendingArm.setTargetPosition(0);
            extendingArm.setPower(-1);
            extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            sleep(1000);
            pixelArm.setTargetPosition(0);
            pixelArm.setPower(.4);
            pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            sleep(1000);


        }




    }


}
