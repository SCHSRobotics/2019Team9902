package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.DcMotor;

public class ClosedLoopDriving {
    MecanumDriver mecanum;
    MecanumEncoders mecanumEncoders;
    MiniPID pidX;
    MiniPID pidY;
    double rotation;
    public ClosedLoopDriving(DcMotor [] M1){
        mecanum = new MecanumDriver(M1);
        //mecanumEncoders = new MecanumEncoders(M1);
        pidX = new MiniPID(1,0, 0);
        pidY = new MiniPID(1,0, 0);

    }
    public void closedLoopDriving(float[] target, float[] actual, boolean targetVisible){
        if(targetVisible) {
            mecanum.mecanumpower((float) pidX.getOutput(actual[0], target[0]), (float) pidY.getOutput(actual[1], target[1]), 0);
        } else {
            mecanumEncoders.mecanumEncoders(target[1]-actual[1], target[0]-actual[0], 0, false); //x and y may need to be flipped
        }
    }
}
