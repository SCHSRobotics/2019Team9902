package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
public class grabberarmdriver {
    public void grabberarm(DcMotor [] Ml, Servo [] Sl) {
        DcMotor linearmotor = Ml[0];
        DcMotor tiltmotor0 = Ml[1];
        DcMotor tiltmotor1 = Ml[2];

        Servo grabmotor = Sl[0];
        Servo twistmotor = Sl[1];

    }
}
