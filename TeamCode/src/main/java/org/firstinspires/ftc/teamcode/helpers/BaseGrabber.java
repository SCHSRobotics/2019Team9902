package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.Servo;

public class BaseGrabber {
    Servo baseServo;
    public BaseGrabber(Servo servo){
        baseServo = servo;
    }
    public void grabberAllTheWayBack(){
        baseServo.setPosition(0);
    }
    public void grabBase(){
        baseServo.setPosition(.9);
    }
    public void realseBase(){
        baseServo.setPosition(.26);
    }
}
