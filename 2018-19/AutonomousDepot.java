package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="AutoDepotState", group="Linear OpMode")
//@Disabled

public class AutonomousDepotState extends LinearOpMode { //Facing the Depot
    private ElapsedTime runtime = new ElapsedTime();
    ElapsedTime time = new ElapsedTime();

    DcMotor[] drive = new DcMotor[4];
    CRServo liftMotor = null;
    Servo grabber = null;

    DigitalChannel limit1;

    boolean first = true;
    int action = 1;

    @Override
    public void runOpMode() {
        for(int c = 0; c < 4; c++) {
            drive[c] = hardwareMap.dcMotor.get("d" + (c + 1));
            drive[c].setPower(0);
            if(c % 2 == 0)
                drive[c].setDirection(DcMotor.Direction.FORWARD);
            else
                drive[c].setDirection(DcMotor.Direction.REVERSE);
            drive[c].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //drive[c].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        
        liftMotor = hardwareMap.crservo.get("lift");
        grabber = hardwareMap.servo.get("grabS");
        limit1 = hardwareMap.digitalChannel.get("lim1");

        liftMotor.setDirection(DcMotor.Direction.FORWARD);
        limit1.setMode(DigitalChannel.Mode.INPUT);
        grabber.setDirection(Servo.Direction.FORWARD);

        waitForStart();
        runtime.reset();
        while(opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            if(first) {
                time.reset();
                first = false;
            }

            switch(action) {
                case 1: //drop from lander
                    if(limit1.getState())
                        liftMotor.setPower(0.8);
                    else {
                        liftMotor.setPower(0);
                        action = 2;
                        first = true;
                    }
                    break;
                case 2: //accelerate away from lander - currently forward!
                    if(time.seconds() < 0.2) {
                        accelerate(time, 0.5, 0, -0.5, drive[0]);
                        accelerate(time, 0.5, 0, 0.5, drive[1]);
                        accelerate(time, 0.5, 0, -0.5, drive[2]);
                        accelerate(time, 0.5, 0, 0.5, drive[3]);
                    }
                    else {
                        action = 3;
                        first = true;
                    }
                    break;
                case 3: //decelerate away from lander
                    if(time.seconds() < 0.2) {
                        accelerate(time,0.5,-0.5,0, drive[0]);
                        accelerate(time,0.5,0.5,0, drive[1]);
                        accelerate(time,0.5,-0.5,0, drive[2]);
                        accelerate(time,0.5,0.5,0, drive[3]);
                    }
                    else {
                        drive[0].setPower(0);
                        drive[1].setPower(0);
                        drive[2].setPower(0);
                        drive[3].setPower(0);
                        action = 10;
                        first = true;
                    }
                    break;
                case 10:
                    if(time.seconds() < 15) {
                        drive[0].setPower(0);
                        drive[1].setPower(0);
                        drive[2].setPower(0);
                        drive[3].setPower(0);
                    }
                    else {
                        action = 4;
                        first = true;
                    }
                    break;
                case 4: //accelerate towards depot
                    if(time.seconds() < 2.1) {
                        accelerate(time, 0.5, 0,-0.5, drive[3]);
                        accelerate(time, 0.5,0,-0.5, drive[0]);
                        accelerate(time,0.5,0,-0.5, drive[1]);
                        accelerate(time,0.5,0,-0.5, drive[2]);
                    }
                    else {
                        action = 5;
                        first = true;
                    }
                    break;
                case 5: //decelerate arriving at depot
                    if(time.seconds() < 0.2) {
                        accelerate(time,0.2,-0.5,0, drive[3]);
                        accelerate(time,0.2,-0.5,0, drive[0]);
                        accelerate(time,0.2,-0.5,0, drive[1]);
                        accelerate(time,0.2,-0.5,0, drive[2]);
                    }
                    else {
                        drive[0].setPower(0);
                        drive[1].setPower(0);
                        drive[2].setPower(0);
                        drive[3].setPower(0);
                        action = 8;
                        first = true;
                    }
                    break;
                case 8:
                    if(time.seconds() < 1) {
                        accelerate(time,0.8,0.5,0, drive[3]);
                        accelerate(time,0.8,-0.5,0, drive[0]);
                        accelerate(time,0.8,-0.5,0, drive[1]);
                        accelerate(time,0.8, 0.5,0, drive[2]);
                    }
                    else {
                        drive[0].setPower(0);
                        drive[1].setPower(0);
                        drive[2].setPower(0);
                        drive[3].setPower(0);
                        action = 6;
                        first = true;
                    }
                    break;
                case 6: //drop marker
                    if(time.seconds() < 0.5) {
                        grabber.setPosition(-1);
                    }
                    else {
                        action = 9;
                        first = true;
                    }
                    break;
                case 9:
                    if(time.seconds() < 0.2) {
                        accelerate(time,0.8,0.5,0, drive[3]);
                        accelerate(time,0.8,-0.5,0, drive[0]);
                        accelerate(time,0.8,-0.5,0, drive[1]);
                        accelerate(time,0.8, 0.5,0, drive[2]);
                    }
                    else {
                        drive[0].setPower(0);
                        drive[1].setPower(0);
                        drive[2].setPower(0);
                        drive[3].setPower(0);
                        action = 7;
                        first = true;
                    }
                    break;
                case 7: //drive toward crater
                    if(time.seconds() < 3) {
                        accelerate(time, 0.5, 0, 0.5, drive[0]);
                        accelerate(time, 0.5, 0, -0.5, drive[1]);
                        accelerate(time, 0.5, 0, 0.5, drive[2]);
                        accelerate(time, 0.5, 0, -0.5, drive[3]);
                        /*accelerate(time, 0.5, 0, -0.5, drive[0]);
                        accelerate(time, 0.5, 0, -0.5, drive[1]);
                        accelerate(time, 0.5, 0, -0.5, drive[2]);
                        accelerate(time, 0.5, 0, -0.5, drive[3]);*/
                    }
                    else {
                        drive[0].setPower(0);
                        drive[1].setPower(0);
                        drive[2].setPower(0);
                        drive[3].setPower(0);
                        action = 0;
                    }
                    break;
            }
        }
    }
    public static void accelerate(ElapsedTime dtime, double period, double speed1, double speed2, DcMotor motor) {
        if(dtime.seconds() < period)
            motor.setPower(((speed2 - speed1) * (dtime.seconds() / period)) + speed1);
        else
            motor.setPower(speed2);
    }
}
