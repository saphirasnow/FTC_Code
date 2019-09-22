package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.*;

@TeleOp(name = "CompBot1 TeleOp 2", group = "Linear Opmode")
//@Disabled

public class CompBot_TeleOp extends LinearOpMode{
    private ElapsedTime runtime = new ElapsedTime();

    DcMotor liftMotor = null;
    DcMotor rightDrive = null;
    DcMotor leftDrive = null;
    Servo rightArm = null;
    Servo leftArm = null;
    Servo leftKnock = null;
    Servo rightKnock = null;
    DeviceInterfaceModule limits = null;

    public static final double MID_SERVO = 0.5;

    @Override
    public void runOpMode() {
        liftMotor = hardwareMap.dcMotor.get("lift");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightArm = hardwareMap.servo.get("rArm");
        leftArm = hardwareMap.servo.get("lArm");
        rightKnock = hardwareMap.servo.get("rightK");
        leftKnock = hardwareMap.servo.get("leftK");
        limits = hardwareMap.deviceInterfaceModule.get("Device Interface Module 1");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftMotor.setPower(0);
        leftDrive.setPower(0);
        rightDrive.setPower(0);

        rightKnock.setPosition(MID_SERVO);
        leftKnock.setPosition(MID_SERVO);

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            Functions.drive(gamepad2, rightDrive, leftDrive);

            if(gamepad1.dpad_up)
                liftMotor.setPower(0.7);
            else if(gamepad1.dpad_down)
                liftMotor.setPower(-0.7);
            else
                liftMotor.setPower(0);

            if(gamepad1.a) {
                leftArm.setPosition(MID_SERVO + 0.4);
                rightArm.setPosition(MID_SERVO - 0.4);
            }
            else if(gamepad1.b) {
                leftArm.setPosition(MID_SERVO - 0.4);
                rightArm.setPosition(MID_SERVO + 0.4);
            }
            else {
                leftArm.setPosition(0.3);
                rightArm.setPosition(0.7);
            }

            rightKnock.setPosition(MID_SERVO);
            leftKnock.setPosition(MID_SERVO);
            idle();
        }
    }
}
