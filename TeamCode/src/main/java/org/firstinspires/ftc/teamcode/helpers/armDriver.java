package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

    double linearSpeed = .8;
    double tiltSpeed = 1;
    //constants
    int wristTurnSpeed = 1;
    int armExtensionSpeed= 5;
    public armDriver(DcMotor [] Ml, Servo [] Sl) {
        linearMotor = Ml[0];
        tiltMotor = Ml[1];

        wristServo = Sl[0];
        grabServo = Sl[1];

        tiltMotor.setDirection(DcMotor.Direction.REVERSE);
        tiltMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        tiltMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


}
    public void tiltArm(float change) {
        double rate = .1;
        tiltArmPos = change*rate;
        if((tiltArmPos < 500) && (tiltArmPos > 0)){ //Change this in order to set the stopping point
            tiltMotor.setTargetPosition((int) tiltArmPos); //converts the double into an int
            tiltMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            tiltMotor.setPower(tiltSpeed);

        }
    }
    public void grab() {
        grabServo.setPosition(0);

    }
    public void realse(){
        grabServo.setPosition(30);

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
           linearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           linearMotor.setPower(linearSpeed);
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