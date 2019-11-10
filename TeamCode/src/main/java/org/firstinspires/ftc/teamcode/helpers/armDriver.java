package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
public class armDriver {
    DcMotor linearMotor;
    DcMotor tiltMotor;

    Servo wristServo;
    Servo grabServo;
    int tiltArmPosition;
    public armDriver(DcMotor [] Ml, Servo [] Sl) {
        linearMotor = Ml[0];
        tiltMotor = Ml[1];

        wristServo = Sl[0];
        grabServo = Sl[1];

    }
    public void tiltArm(int position) {
        if (position < 500 && position > 0) { //Change this in order to set the endstop
            tiltMotor.setTargetPosition(position);
        }
    }
    public void grabberHand(Servo grabServo, Servo wristServo, int grabPos, int wristPos) {
        grabServo.setPosition(grabPos);
        wristServo.setPosition(wristPos);
    }
    public void linearArm(int position) {
        if (position < 500) {
            linearMotor.setTargetPosition(position);
        }
    }
}
