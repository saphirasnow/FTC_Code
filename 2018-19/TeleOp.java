package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TeleOpState", group = "LinearOpMode")
//@Disabled

public class TeleOpState extends LinearOpMode{
    private ElapsedTime runtime = new ElapsedTime();

    DcMotor[] drive = new DcMotor[4];
    CRServo liftMotor = null;
    Servo grabber = null;

    DigitalChannel limit1;

    int currentMotion = 0;
    ElapsedTime dtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        for(int c = 0; c < 4; c++) {
            drive[c] = hardwareMap.dcMotor.get("d" + (c + 1));
            drive[c].setPower(0);
            if(c % 2 == 0)
                drive[c].setDirection(DcMotor.Direction.FORWARD);
            else
                drive[c].setDirection(DcMotor.Direction.REVERSE);
            drive[c].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            //drive[c].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        liftMotor = hardwareMap.crservo.get("lift");
        grabber = hardwareMap.servo.get("grabS");

        liftMotor.setPower(0);
        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        grabber.setDirection(Servo.Direction.FORWARD);

        limit1 = hardwareMap.digitalChannel.get("lim1");
        limit1.setMode(DigitalChannel.Mode.INPUT);

        waitForStart();
        runtime.reset();
        while(opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            currentMotion = drive(currentMotion, 0.5, 1, 0.5);

            if(gamepad1.b) //arm
                liftMotor.setPower(1); //down
            else if(gamepad1.y && limit1.getState())
                liftMotor.setPower(-1); //up
            else
                liftMotor.setPower(0);

            if(gamepad1.left_trigger > 0.1) //grabber
                grabber.setPosition(0.6);
            else if(gamepad1.right_trigger > 0.1)
                grabber.setPosition(-1);
        }
    }
    public int drive(int currentMotion, double period, double dspeed, double tspeed) {
        double d1 = 0, d2 = 0, d3 = 0, d4 = 0;

        if(gamepad1.left_stick_y > 0.1 && (gamepad1.left_stick_x > -0.3 && gamepad1.left_stick_x < 0.3)) { //up
            if(currentMotion != 1) {
                currentMotion = 1;
                dtime.reset();
            }
            d1 = accelerate(period, 0, dspeed, drive[0]);
            d2 = accelerate(period, 0, -1 * dspeed, drive[1]);
            d3 = accelerate(period, 0, dspeed, drive[2]);
            d4 = accelerate(period, 0, -1 * dspeed, drive[3]);
        }
        else if(gamepad1.left_stick_y < -0.1 && (gamepad1.left_stick_x > -0.3 && gamepad1.left_stick_x < 0.3)) { //down
            if(currentMotion != 2) {
                currentMotion = 2;
                dtime.reset();
            }
            d1 = accelerate(period, 0, -1 * dspeed, drive[0]);
            d2 = accelerate(period, 0, dspeed, drive[1]);
            d3 = accelerate(period, 0, -1 * dspeed, drive[2]);
            d4 = accelerate(period, 0, dspeed, drive[3]);
        }
        else if(gamepad1.left_stick_x > 0.1 && (gamepad1.left_stick_y > -0.3 && gamepad1.left_stick_y < 0.3)) { //right
            if(currentMotion != 3) {
                currentMotion = 3;
                dtime.reset();
            }
            d1 = accelerate(period, 0, -1 * dspeed, drive[0]);
            d2 = accelerate(period, 0, -1 * dspeed, drive[1]);
            d3 = accelerate(period, 0, -1 * dspeed, drive[2]);
            d4 = accelerate(period, 0, -1 * dspeed, drive[3]);
        }
        else if(gamepad1.left_stick_x < -0.1 && (gamepad1.left_stick_y > -0.3 && gamepad1.left_stick_y < 0.3)) { //left
            if(currentMotion != 4) {
                currentMotion = 4;
                dtime.reset();
            }
            d1 = accelerate(period, 0, dspeed, drive[0]);
            d2 = accelerate(period, 0, dspeed, drive[1]);
            d3 = accelerate(period, 0, dspeed, drive[2]);
            d4 = accelerate(period, 0, dspeed, drive[3]);
        }
        else if(gamepad1.left_stick_y < -0.1 && gamepad1.left_stick_x > 0.1) { //up-right
            if(currentMotion != 5) {
                currentMotion = 5;
                dtime.reset();
            }
            d1 = accelerate(period, 0, -1 * dspeed, drive[0]);
            d3 = accelerate(period, 0, -1 * dspeed, drive[2]);
        }
        else if(gamepad1.left_stick_y < -0.1 && gamepad1.left_stick_x < -0.1) { //up-left
            if(currentMotion != 6) {
                currentMotion = 6;
                dtime.reset();
            }
            d2 = accelerate(period, 0, dspeed, drive[1]);
            d4 = accelerate(period, 0, dspeed, drive[3]);
        }
        else if(gamepad1.left_stick_y > 0.1 && gamepad1.left_stick_x > 0.1) { //down-right
            if(currentMotion != 7) {
                currentMotion = 7;
                dtime.reset();
            }
            d2 = accelerate(period, 0, -1 * dspeed, drive[1]);
            d4 = accelerate(period, 0, -1 * dspeed, drive[3]);
        }
        else if(gamepad1.left_stick_y > 0.1 && gamepad1.left_stick_x < -0.1) { //down-left
            if(currentMotion != 8) {
                currentMotion = 8;
                dtime.reset();
            }
            d1 = accelerate(period, 0, dspeed, drive[0]);
            d3 = accelerate(period, 0, dspeed, drive[2]);
        }
        else {
            if(currentMotion != 0) {
                currentMotion = 0;
                dtime.reset();
            }
            d1 = 0;
            d2 = 0;
            d3 = 0;
            d4 = 0;
        }

        if(gamepad1.right_stick_x > 0.1) { //turn right
            if(d1 > -0.7)
                d1 = d1 - 0.3;
            if(d2 > -0.7)
                d2 = d2 - 0.3;
            if(d3 < 0.7)
                d3 = d3 + 0.3;
            if(d4 < 0.7)
                d4 = d4 + 0.3;
        }
        else if(gamepad1.right_stick_x < -0.1) { //turn left
            if(d1 < 0.7)
                d1 = d1 + 0.3;
            if(d2 < 0.7)
                d2 = d2 + 0.3;
            if(d3 > -0.7)
                d3 = d3 - 0.3;
            if(d4 > -0.7)
                d4 = d4 - 0.3;
        }

        drive[0].setPower(d1);
        drive[1].setPower(d2);
        drive[2].setPower(d3);
        drive[3].setPower(d4);

        return currentMotion;
    }
    public double accelerate(double period, double speed1, double speed2, DcMotor motor) {
        if (dtime.seconds() < period)
            return (((speed2 - speed1) * (dtime.seconds() / period)) + speed1);
        else
            return speed2;
    }
}
