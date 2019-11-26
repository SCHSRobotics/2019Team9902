package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
public class ArmDriver {
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

    int tiltArmMax = 1467;
    int armExtensionMax = 4800;
    //constants
    int wristTurnSpeed = 1;
    int armExtensionSpeed= 5;
    public ArmDriver(DcMotor [] Ml, Servo [] Sl) {
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
        double rate = 1;
        tiltArmPos += change*rate;
        if((tiltArmPos < tiltArmMax) && (tiltArmPos > 0)){ //Change this in order to set the stopping point
            tiltMotor.setTargetPosition((int) tiltArmPos); //converts the double into an int
            tiltMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            tiltMotor.setPower(tiltSpeed);

        }
        if(tiltArmPos > tiltArmMax) tiltArmPos = tiltArmMax;
        if(tiltArmPos < 0) tiltArmPos = 0;
    }
    public void grab() {
        grabServo.setPosition(0);

    }
    public void release(){
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
        if ((position < 50000) && (position > 0)){
           linearMotor.setTargetPosition(position);
           linearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           linearMotor.setPower(tiltSpeed);
        }
    }
    public void extendArm() {
        if(armExtensionPosition > armExtensionMax) armExtensionPosition = armExtensionMax;
        armExtensionPosition = armExtensionPosition+armExtensionSpeed;
        linearArm(armExtensionPosition);
    }
    public void retractArm(){
        if(armExtensionPosition < 0) armExtensionPosition = 0;
        armExtensionPosition = armExtensionPosition-armExtensionSpeed;
        linearArm(armExtensionPosition);
    }

}