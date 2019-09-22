package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.*;
import org.firstinspires.ftc.robotcore.external.navigation.*;

@Autonomous(name = "CompBot Auto Red-Back", group = "Linear Opmode")
//@Disabled

public class CompBot_Auto_RB extends LinearOpMode{ //Autonomous Program for Red-Back balancing stone
    private ElapsedTime runtime = new ElapsedTime();

    DcMotor rightDrive = null;
    DcMotor leftDrive = null;
    DcMotor lift = null;
    Servo leftKnock = null;
    Servo rightKnock = null;
    Servo rightArm = null;
    Servo leftArm = null;
    ColorSensor rightSensor = null;
    ColorSensor leftSensor = null;
    VuforiaLocalizer vuforia = null;

    public static final double MID_SERVO = 0.5;

    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "";

        lift = hardwareMap.dcMotor.get("lift");
        rightDrive = hardwareMap.dcMotor.get("rightD");
        leftDrive = hardwareMap.dcMotor.get("leftD");
        rightKnock = hardwareMap.servo.get("rightK");
        leftKnock = hardwareMap.servo.get("leftK");
        rightArm = hardwareMap.servo.get("rightA");
        leftArm = hardwareMap.servo.get("leftA");
        rightSensor = hardwareMap.colorSensor.get("rightC");
        leftSensor = hardwareMap.colorSensor.get("leftC");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lift.setPower(0);
        leftDrive.setPower(0);
        rightDrive.setPower(0);

        rightKnock.setPosition(MID_SERVO);
        leftKnock.setPosition(MID_SERVO);

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK; //FRONT if on opposite side?? Test This!!!!!
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");

        int step = 1;
        String mark = "";
        ElapsedTime t = new ElapsedTime();

        waitForStart();
        runtime.reset();
        relicTrackables.activate();

        while(opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Cipher: ", Functions.readCipher(telemetry, vuforia));
            telemetry.addData("R Color (red):", rightSensor.red());
            telemetry.addData("R Color (blue):", rightSensor.blue());
            telemetry.addData("L Color (red):", leftSensor.red());
            telemetry.addData("L Color (blue):", leftSensor.blue());
            telemetry.update();

            switch(step) {
                case 1: mark = Functions.readCipher(telemetry, vuforia); //read the cipher
                    if (mark.equals(""))
                        mark = "CENTER"; //default value in case the reading doesn't go well
                    step = 2;
                    break;
                case 2: rightKnock.setPosition(Servo.MAX_POSITION); //hopefully this is down
                    double power = 0;
                    if(Functions.checkColor(rightSensor, "RED")) //use "RED" as necessary
                        power = 0.5;
                    else
                        power = -0.5;
                    t.reset();
                    while(t.time() < 0.5)
                        rightDrive.setPower(power);
                    rightDrive.setPower(0);
                    rightKnock.setPosition(MID_SERVO);
                    step = 3;
                    break;
                case 3: Functions.driveAuto(2, 0.5, rightDrive, leftDrive); //drive to Cryptobox
                    t.reset();
                    while(t.time() < 1) {
                        rightDrive.setPower(-0.5);
                        leftDrive.setPower(0.5);
                    }
                    rightDrive.setPower(0);
                    leftDrive.setPower(0);
                    step = 4;
                    break;
                case 4: double ti = 0; //go to the appropriate column - change values for other side
                    switch(mark) {
                        case "CENTER": ti = 0.5; break;
                        case "LEFT": ti = 1; break; //Assuming that "RIGHT" is the ROBOT'S "RIGHT"!!!
                    }
                    Functions.driveAuto(ti, 0.5, rightDrive, leftDrive);
                    step = 5;
                    break;
                case 5: t.reset();
                    while(t.time() < 1) {
                        rightDrive.setPower(-0.5);
                        leftDrive.setPower(0.5);
                    }
                    rightDrive.setPower(0);
                    leftDrive.setPower(0);
                    leftArm.setPosition(MID_SERVO + 0.5);
                    rightArm.setPosition(MID_SERVO - 0.5);
                    step = 0;
                    break;
            }

            idle();
        }
    }
}
