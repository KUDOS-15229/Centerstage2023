package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class DwayneHardware {

    private LinearOpMode myOpMode = null;   // gain access to methods in the calling OpMode.
    DcMotor frontLeftMotor  = null;
    DcMotor backLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backRightMotor = null;
    DcMotor extendingArm = null;
    DcMotor pixelArm = null;
    DcMotor intakeMotor1 = null;
    DcMotor intakeMotor2 = null;
    Servo airplaneServo = null;
    Servo dropPixel = null;

    public void RobotHardware (LinearOpMode opmode) {
        myOpMode = opmode;
    }
public void init() {

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
}

public void placePixelForAuton()     {
    pixelArm.setTargetPosition(810);
    pixelArm.setPower(-1);
    pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    myOpMode.sleep(1000);
    pixelArm.setPower(.1);
    extendingArm.setTargetPosition(2000);
    extendingArm.setPower(1);
    extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    myOpMode.sleep(1000);
    dropPixel.setPosition(.5);
    myOpMode.sleep(1000);
    dropPixel.setPosition(dropPixel.getPosition()+.5);
    extendingArm.setTargetPosition(0);
    extendingArm.setPower(-1);
    extendingArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    myOpMode.sleep(1000);
    pixelArm.setTargetPosition(0);
    pixelArm.setPower(.4);
    pixelArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    myOpMode.sleep(1000);
}


public void spitOut()         {
    //spin the intake motors backwards very gently to release pixel
    intakeMotor1.setPower(-0.5);
    intakeMotor2.setPower(0.5);
    myOpMode.sleep(750);
    intakeMotor1.setPower(0);
    intakeMotor2.setPower(0);
}








}