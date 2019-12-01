package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.DcMotor;

import static java.lang.Math.PI;

public class MecanumEncoders {
    DcMotor fl;
    DcMotor bl;
    DcMotor fr;
    DcMotor br;
    double wheelDiameter = 100; //duh, in mm
    double strafeCoeff = .5; //how much slower the strafe is. Decimal Percentage.
    final float mmPerInch = 25.4f; //yoinked straight from vuforia
    double encoderPerRev = 95.9; //either 95.9 or 383.6


    double inchesPerRev = wheelDiameter*mmPerInch*PI;
    double rotationRadius = Math.sqrt(Math.pow(198.25, 2)+Math.pow(168, 2));
    public MecanumEncoders(DcMotor[] M1){
        fl = M1[0];
        fr = M1[1];
        bl = M1[2];
        br = M1[3];


    }
    public void mecanumEncoders(double y, double x, double r){
        y = y*inchesPerRev;
        x = x*inchesPerRev*strafeCoeff;
        r = r*rotationRadius*inchesPerRev; //if we need to rotate were gonna need to determine the legenth from the center of the 4 wheels, make a circle ouut of that, then multiply this by the diameter and stuff
        if(fl.getMode()!= DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
            fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        double flRT = -(y - x + r);
        double blRT = -(y + x + r);
        double frRT = (y + x - r);
        double brRT = (y - x - r);
        fl.setTargetPosition((int)(flRT*encoderPerRev));
        fr.setTargetPosition((int)(frRT*encoderPerRev));
        bl.setTargetPosition((int)(blRT*encoderPerRev));
        br.setTargetPosition((int)(brRT*encoderPerRev));
    }
}
