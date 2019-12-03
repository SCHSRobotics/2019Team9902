package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.Servo;

public class StickDriver {
    Servo stickServo;
    public StickDriver(Servo servo){
        stickServo = servo;
    }
    public void stickUp(){
        stickServo.setPosition(.32);
    }
    public void stickDown(){
        stickServo.setPosition(0);
    }
}
