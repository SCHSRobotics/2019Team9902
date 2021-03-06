package org.firstinspires.ftc.teamcode.helpers;

import android.os.Debug;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.floorMod;
import static java.lang.Math.min;
import static java.lang.Thread.sleep;

public class MecanumEncoders {
    DcMotorEx fl;
    DcMotorEx bl;
    DcMotorEx fr;
    DcMotorEx br;
    private double rampUpAndDownTime = 500;
    private double accelStart = .4;
    private double flAccel;
    private double blAccel;
    private double brAccel;
    private double frAccel;
    private double accel = 0.1;
    public boolean customSpeed;
    public boolean ready = false;
    double power = .8;
    double wheelDiameter = 100d; //selfexplanitory, in mm
    double strafeCoeff = 2d; //how much slower the strafe is. Decimal Percentage.
    final float mmPerInch = 25.4f; //yoinked straight from vuforia
    double encoderPerRev = 188.3; //either 188.3 or 753.2
    double gearRatio = 2d;
    double coeff = 0.73136870272d;
    double motorDeadzone = 8d;
    double PPMM = (wheelDiameter * PI / encoderPerRev) * gearRatio;
    double PPIN = PPMM * mmPerInch * coeff;
    double rotationRadius = Math.sqrt(Math.pow(198.25, 2) + Math.pow(168, 2));
    double rotationCircleCircumfrence = 2 * rotationRadius * PI;
    double rotationCircle = rotationCircleCircumfrence * PPMM;
    public DcMotor[] motorList;

    public MecanumEncoders(DcMotorEx[] M1) {
        fl = M1[0];
        fr = M1[1];
        bl = M1[2];
        br = M1[3];
        motorList = M1;
    }
    public void mecanumEncoders(double y, double x, double r, boolean waitForEnd, double speed){
        power = speed;
        mecanumEncoders(y, x, r, waitForEnd);
        power = .8;
    }
    public void mecanumEncoders(double y, double x, double r, boolean waitForEnd) {

        y = y * PPIN;
        x = x * PPIN * strafeCoeff;
        r = r * rotationCircle; //if we need to rotate were gonna need to determine the legenth from the center of the 4 wheels, make a circle out of that, then multiply this by the diameter and stuff
        double flRT = (y - x + r);
        double blRT = (y + x + r);
        double frRT = -(y + x - r);
        double brRT = -(y - x - r);
        int flTP = (int) (flRT);
        int frTP = (int) (frRT);
        int blTP = (int) (blRT);
        int brTP = (int) (brRT);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setTargetPosition(flTP);
        fr.setTargetPosition(frTP);
        bl.setTargetPosition(blTP);
        br.setTargetPosition(brTP);
        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fl.setPower(power);
        bl.setPower(power);
        fr.setPower(power);
        br.setPower(power);
        while ((abs(fl.getTargetPosition() - fl.getCurrentPosition()) > motorDeadzone) && (abs(bl.getTargetPosition() - bl.getCurrentPosition()) > motorDeadzone) && (abs(fr.getTargetPosition() - fr.getCurrentPosition()) > motorDeadzone) && (abs(br.getTargetPosition() - br.getCurrentPosition()) > motorDeadzone)) {
            try {
                sleep(5);
            } catch (InterruptedException e) {

            }
        }
       /* if (waitForEnd) {
            while ((abs(fl.getTargetPosition() - fl.getCurrentPosition()) > motorDeadzone) && (abs(bl.getTargetPosition() - bl.getCurrentPosition()) > motorDeadzone) && (abs(fr.getTargetPosition() - fr.getCurrentPosition()) > motorDeadzone) && (abs(br.getTargetPosition() - br.getCurrentPosition()) > motorDeadzone)) {
                    if (fl.getCurrentPosition() < rampUpAndDownTime) {
                        power = Math.max((fl.getCurrentPosition() / rampUpAndDownTime), accelStart);
                    } else if (fl.getTargetPosition() - fl.getCurrentPosition() < rampUpAndDownTime) {
                        power = (fl.getCurrentPosition() - fl.getCurrentPosition()) / rampUpAndDownTime;
                    } else {
                        power = 1;
                    }
                    fl.setPower(power);
                    bl.setPower(power);
                    fr.setPower(power);
                    br.setPower(power);
                try {
                    sleep(5);
                } catch (InterruptedException e) {
                }
            }*/
            }
        }
