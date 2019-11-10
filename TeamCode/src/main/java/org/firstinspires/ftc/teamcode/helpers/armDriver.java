package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
public class armDriver {
    public void grabberArm(DcMotor [] Ml, Servo [] Sl, int tiltPosition, int linearExtension, int wristPos, int grabPos) {
        DcMotor linearMotor = Ml[0];
        DcMotor tiltMotor = Ml[1];

        Servo wristServo = Sl[0];
        Servo grabServo = Sl[1];

        tiltArm(tiltMotor, tiltPosition);
        linearArm(linearMotor, linearExtension);
        grabberHand(wristServo, grabServo, wristPos, grabPos);
    }
    private void tiltArm(DcMotor tiltMotor, int position) {
        if (position < 500) {
            tiltMotor.setTargetPosition(position);
        }
    }
    private void grabberHand(Servo grabServo, Servo wristServo, int grabPos, int wristPos) {
        grabServo.setPosition(grabPos);
        wristServo.setPosition(wristPos);
    }
    private void linearArm(DcMotor linearMotor, int position) {
        if (position < 500) {
            linearMotor.setTargetPosition(position);
        }
    }
}
