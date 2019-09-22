package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="AutoCraterStateT", group="Linear OpMode")
//@Disabled

public class AutonomousCraterStateTry extends LinearOpMode { //Facing the Crater
    private ElapsedTime runtime = new ElapsedTime();
    ElapsedTime time = new ElapsedTime();

    DcMotor[] drive = new DcMotor[4];
    CRServo liftMotor = null;
    Servo grabber = null;

    DigitalChannel limit1; //magnetic limit switch - true when not sensing, false when sensing

    boolean first = false;
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

        limit1 = hardwareMap.digitalChannel.get("lim1");
        grabber = hardwareMap.servo.get("grabS");
        grabber.setDirection(Servo.Direction.FORWARD);
        
        liftMotor.setDirection(DcMotor.Direction.FORWARD);

        limit1.setMode(DigitalChannel.Mode.INPUT);
        
        //liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        runtime.reset();
        while(opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("limit: ", limit1.getState());
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
                case 2: //accelerate away from lander
                    if(time.seconds() < 0.2) {
                        AutonomousDepotState.accelerate(time, 0.5, 0, -0.5, drive[0]);
                        AutonomousDepotState.accelerate(time, 0.5, 0, 0.5, drive[1]);
                        AutonomousDepotState.accelerate(time, 0.5, 0, -0.5, drive[2]);
                        AutonomousDepotState.accelerate(time, 0.5, 0, 0.5, drive[3]);
                    }
                    else {
                        action = 3;
                        first = true;
                    }
                    break;
                case 3: //decelerate away from lander
                    if(time.seconds() < 0.2) {
                        AutonomousDepotState.accelerate(time,0.5,-0.5,0, drive[0]);
                        AutonomousDepotState.accelerate(time,0.5,0.5,0, drive[1]);
                        AutonomousDepotState.accelerate(time,0.5,-0.5,0, drive[2]);
                        AutonomousDepotState.accelerate(time,0.5,0.5,0, drive[3]);
                    }
                    else {
                        drive[0].setPower(0);
                        drive[1].setPower(0);
                        drive[2].setPower(0);
                        drive[3].setPower(0);
                        action = 4;
                        first = true;
                    }
                    break;
                case 4: //accelerate towards crater
                    if(time.seconds() < 1) {
                        AutonomousDepotState.accelerate(time, 0.5, 0,-0.5, drive[3]);
                        AutonomousDepotState.accelerate(time, 0.5,0,-0.5, drive[0]);
                        AutonomousDepotState.accelerate(time,0.5,0,-0.5, drive[1]);
                        AutonomousDepotState.accelerate(time,0.5,0,-0.5, drive[2]);
                    }
                    else {
                        action = 5;
                        first = true;
                    }
                    break;
                case 5: //decelerate arriving at crater
                    if(time.seconds() < 0.4) {
                        AutonomousDepotState.accelerate(time,0.5,-0.5,0, drive[3]);
                        AutonomousDepotState.accelerate(time,0.5,-0.5,0, drive[0]);
                        AutonomousDepotState.accelerate(time,0.5,-0.5,0, drive[1]);
                        AutonomousDepotState.accelerate(time,0.5,-0.5,0, drive[2]);
                    }
                    else {
                        drive[0].setPower(0);
                        drive[1].setPower(0);
                        drive[2].setPower(0);
                        drive[3].setPower(0);
                        action = 6;
                    }
                    break;
                case 6:
                    if(time.seconds() < 0.5) {
                        AutonomousDepotState.accelerate(time,0.5,0,0.5, drive[1]);
                        AutonomousDepotState.accelerate(time,0.5,0,0.5, drive[3]);
                    }
                    else {
                        drive[0].setPower(0);
                        drive[1].setPower(0);
                        drive[2].setPower(0);
                        drive[3].setPower(0);
                        action = 7;
                    }
                    break;
                case 7:
                    if(time.seconds() < 0.5) {
                        grabber.setPosition(-1);
                    }
                    else {
                        action = 8;
                        first = true;
                    }
                        break;
                case 8:
                    if(time.seconds() < 3) {
                        AutonomousDepotState.accelerate(time,0.8,0.5,0, drive[3]);
                        AutonomousDepotState.accelerate(time,0.8,-0.5,0, drive[0]);
                        AutonomousDepotState.accelerate(time,0.8,-0.5,0, drive[1]);
                        AutonomousDepotState.accelerate(time,0.8,0.5,0, drive[2]);
                    }
                    else {
                        drive[0].setPower(0);
                        drive[1].setPower(0);
                        drive[2].setPower(0);
                        drive[3].setPower(0);
                        action = 9;
                        first = true;
                    }
                    break;
                case 9: //drive toward crater
                    if(time.seconds() < 3) {
                        AutonomousDepotState.accelerate(time,0.5,0.5,0, drive[0]);
                        AutonomousDepotState.accelerate(time,0.5,-0.5,0, drive[1]);
                        AutonomousDepotState.accelerate(time,0.5,0.5,0, drive[2]);
                        AutonomousDepotState.accelerate(time,0.5,-0.5,0, drive[3]);
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
}
