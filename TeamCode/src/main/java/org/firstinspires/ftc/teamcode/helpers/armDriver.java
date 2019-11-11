package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
public class armDriver {
    DcMotor linearMotor;
    DcMotor tiltMotor;

    Servo wristServo;
    Servo grabServo;
    int setGrabPos;
    int wristPos;
    double tiltArmPos = 0;
    int armExtensionPosition;




    //constants
    int wristTurnSpeed = 1;
    int armExtensionSpeed= 5;
    public armDriver(DcMotor [] Ml, Servo [] Sl) {
        linearMotor = Ml[0];
        tiltMotor = Ml[1];

        wristServo = Sl[0];
        grabServo = Sl[1];

}
    public void tiltArm(float change) {
        double rate = .1;
        tiltArmPos = change*rate;
        if((tiltArmPos < 500) && (tiltArmPos > 0)){ //Change this in order to set the stopping point
            tiltMotor.setTargetPosition((int) tiltArmPos); //converts the double into an int
        }
    }
    public void grab() {
        grabServo.setPosition(50);

    }
    public void realse(){
        grabServo.setPosition(80);

    }
    public void grabberWrist(int wristPos){
        if((wristPos <= 180) && (wristPos >= 0));
        wristServo.setPosition(wristPos);

    }
    public void turnGrabberCW(){
        wristPos = wristPos+wristTurnSpeed;
        grabberWrist(wristPos);
    }
    public void turnGrabberCCW(){
        wristPos = wristPos-wristTurnSpeed;
        grabberWrist(wristPos);
    }
    public void linearArm(int position) {
        if (position < 500) {
            linearMotor.setTargetPosition(position);
        }
    }
    public void extendArm() {
        armExtensionPosition = armExtensionPosition+armExtensionSpeed;
        linearArm(armExtensionPosition);
    }
    public void retractArm(){
        armExtensionPosition = armExtensionPosition-armExtensionSpeed;
        linearArm(armExtensionPosition);
    }
}