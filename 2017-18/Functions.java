package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.*;

public class Functions {
    public static String readCipher(Telemetry telemetry, VuforiaLocalizer vuforia) {
        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTrackables.activate();

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            telemetry.addData("VuMark", "%s visible", vuMark);
            telemetry.update();
            return vuMark.toString();
        }
        else
            telemetry.addData("VuMark", "not visible");
        telemetry.update();
        return "";
    }
    public static boolean checkColor(ColorSensor s, String c) {
        if(c == "Blue") {
            if(s.red() > s.blue())
                return false;
            else
                return true;
        }
        else {
            if(s.red() < s.blue())
                return false;
            else
                return true;
        }
    }
    public static void driveAuto(double time, double speed, DcMotor rightDrive, DcMotor leftDrive) {
        ElapsedTime t = new ElapsedTime();
        while(t.time() < time) {
            rightDrive.setPower(speed);
            leftDrive.setPower(speed

            );
        }
        rightDrive.setPower(0);
        leftDrive.setPower(0);
    }
    public static void drop(DcMotor gm, DcMotor m, DeviceInterfaceModule cdi) {
        ElapsedTime t = new ElapsedTime();
        while(!cdi.getDigitalChannelState(2)) {
            m.setPower(0.5);
        }
        m.setPower(-0.1);
        t.reset();
        while(Integer.parseInt(t.toString()) < 0.5) {
            m.setPower(-0.1);
        }
        m.setPower(0);
        t.reset();
        while(Integer.parseInt(t.toString()) < 1 && (!cdi.getDigitalChannelState(0) && !cdi.getDigitalChannelState(1))) {
            gm.setPower(0.1);
        }
        gm.setPower(0);
    }
    public static void drive(Gamepad g, DcMotor mR, DcMotor mL) {
        if(g.a) { //g drives
            if(g.left_stick_x < -0.1) {
                mL.setPower(1);
                mR.setPower(-(g.left_stick_x / 10.0));
            }
            else if(g.left_stick_x > 0.1) {
                mL.setPower(g.left_stick_x / 10.0);
                mR.setPower(1);
            }
            else {
                mL.setPower(1);
                mR.setPower(1);
            }
        }
        else if(g.b) {
            if(g.left_stick_x < -0.1) {
                mL.setPower(-1);
                mR.setPower(g.left_stick_x / 10.0);
            }
            else if(g.left_stick_x > 0.1) {
                mL.setPower(-(g.left_stick_x / 10.0));
                mR.setPower(-1);
            }
            else {
                mL.setPower(-1);
                mR.setPower(-1);
            }
        }
        else {
            mL.setPower(0);
            mR.setPower(0);
        }
    }
    public static void throwRelic(Servo l, Servo r, DcMotor arm) {
        final double MID_SERVO = 0.5;
        ElapsedTime t = new ElapsedTime();
        t.reset();
        while(t.time() < 0.5) {
            l.setPosition(MID_SERVO + 0.5);
            r.setPosition(MID_SERVO + 0.5);
            if(t.time() > 0.2)
                arm.setPower(-0.1);
        }
        arm.setPower(0);
        l.setPosition(MID_SERVO);
        r.setPosition(MID_SERVO);
    }
}
