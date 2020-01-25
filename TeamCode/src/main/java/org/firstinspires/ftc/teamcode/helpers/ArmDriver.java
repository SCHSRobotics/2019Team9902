package org.firstinspires.ftc.teamcode.helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
public class ArmDriver {
    public boolean doServo= true;
    DcMotor linearMotor;
    DcMotor tiltMotor;

    Servo wristServo;
    Servo grabServo;
    Servo tiltServo;
    public double wristPos = 0; //starting position
    public double tiltArmPos = 0;
    int armExtensionPosition;

    double tiltSpeed = 2;
    public double armPos = 0d;
    int tiltArmMax = 1467;
    int armExtensionMax = 2450;
    //constants
    double wristTurnSpeedCoeff = .008;
    int armExtensionSpeed= 25;
    double encoderPerDegree = 5;
    public ArmDriver(DcMotor [] Ml, Servo [] servos) {
        linearMotor = Ml[1];
        tiltMotor = Ml[0];

        grabServo = servos[0];
        wristServo = servos[1];
        tiltServo = servos[2];
        wristServo.setPosition(wristPos);
        grabServo.setDirection(Servo.Direction.FORWARD);

        tiltMotor.setDirection(DcMotor.Direction.REVERSE);
        tiltMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        tiltMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void tiltArm(float change) {
        tiltArmPos += change*8d;
        if((tiltArmPos < tiltArmMax) && (tiltArmPos > 0)){ //Change this in order to set the stopping point
            tiltMotor.setTargetPosition((int) tiltArmPos); //converts the double into an int
            tiltMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            tiltMotor.setPower(tiltSpeed);
        }
        if(doServo){
        double encoderPerDegree = 7;
        tiltServo.setPosition(1-tiltMotor.getCurrentPosition()/encoderPerDegree/280);
        if(tiltArmPos > tiltArmMax) tiltArmPos = tiltArmMax;
        if(tiltArmPos < 0) tiltArmPos = 0;}
    }
    public void grab() {
        grabServo.setPosition(.80);

    }
    public void open(){
        grabServo.setPosition(.95);

    }
    public void grabberWrist(double wristPos){
        if((wristPos <= 1)  && (wristPos >= 0)) {
            wristServo.setPosition(wristPos);
        }
    }

    public void turnGrabber(double wristTurnSpeed){
        wristPos = (wristPos + wristTurnSpeed*wristTurnSpeedCoeff);
        if(wristPos > 1) {
            wristPos = 1;
        }
        if(wristPos < 0){
            wristPos = 0;
        }
        grabberWrist(wristPos);
    }
    public void linearArm(int position) {
        if ((position < armExtensionMax) && (position > 0)){
           linearMotor.setTargetPosition(position);
           linearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           linearMotor.setPower(tiltSpeed);
        }
    }
    public void linearArmChg(double change){
        armPos = armPos +(change*armExtensionSpeed);
        if(armPos > armExtensionMax){
            armPos = armExtensionMax;
        }
        if(armPos < 0){
            armPos = 0;
        }
        linearArm((int)armPos);
    }
    public void extendArm() {
        if(armExtensionPosition > armExtensionMax) {
            armExtensionPosition = armExtensionMax;
        }
        armExtensionPosition = armExtensionPosition+armExtensionSpeed;
        linearArm(armExtensionPosition);
    }
    public void retractArm(){
        if(armExtensionPosition < 0) armExtensionPosition = 0;
        armExtensionPosition = armExtensionPosition-armExtensionSpeed;
        linearArm(armExtensionPosition);
    }
}