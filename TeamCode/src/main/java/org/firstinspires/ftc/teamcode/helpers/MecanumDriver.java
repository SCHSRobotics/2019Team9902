package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Made by Itmm on 10/12/2019
 * will hopefully deal with most of the mecanum equations in driver controlled mode and autonomous mode
*/


public class MecanumDriver {
    //Call this function with y x and r values with y being forward backwards, x being strafe left right, and r being rotation. This is for driver controlled mode so no encoders or units
    // PWR = power
    DcMotor fl;
    DcMotor bl;
    DcMotor fr;
    DcMotor br;
    public MecanumDriver(DcMotor [] M1){
         fl = M1[0];
         fr = M1[1];
         bl = M1[2];
         br = M1[3];
    }
    public void mecanumpower(float y, float x, float r) {
        float flPWR = -(y - x + r);
        float blPWR = -(y + x + r);
        float frPWR = (y + x - r);
        float brPWR = (y - x - r);
        fl.setPower(flPWR);
        bl.setPower(blPWR);
        fr.setPower(frPWR);
        br.setPower(brPWR);
    }

}
