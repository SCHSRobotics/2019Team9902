/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.helpers.ArmDriver;
import org.firstinspires.ftc.teamcode.helpers.ClosedLoopDriving;
import org.firstinspires.ftc.teamcode.helpers.MecanumDriver;
import org.firstinspires.ftc.teamcode.helpers.MecanumEncoders;
import org.firstinspires.ftc.teamcode.helpers.Position;
import org.firstinspires.ftc.teamcode.helpers.VuforiaNavigation;
import org.firstinspires.ftc.teamcode.helpers.VuforiaStone;

import static org.firstinspires.ftc.teamcode.helpers.Position.position.CENTER;
import static org.firstinspires.ftc.teamcode.helpers.Position.position.LEFT;
import static org.firstinspires.ftc.teamcode.helpers.Position.position.RIGHT;

//import org.firstinspires.ftc.teamcode.helpers.vuforia;

/**
 Non Linear Main OP mode
 Itmm 10/12/19 with plenty of code from sketchy hacker kid
 */

@Autonomous(name="AutoOP", group="Testing")
public class AutoTest extends LinearOpMode {
    public int cameraMonitorViewId;
    //starts the class things up here so they can be used in all of the things
    MecanumDriver mecanum;
    MecanumEncoders mE;
    ArmDriver gA;
    ClosedLoopDriving closedLoopDriver;
    public int posOffset = 0; //The block positioning offset that we have. this will be different for different starting points

    @Override
    public void runOpMode() {
        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //make the helper classes
        telemetry.addData("Status", "Start init");
        WebcamName webcam0 = hardwareMap.get(WebcamName.class, "Webcam 1");
        VuforiaNavigation vn = new VuforiaNavigation();
        VuforiaStone vs = new VuforiaStone();
        vs.setup(webcam0);
        //vn.setup(webcam0);
        Servo releaseServo = hardwareMap.servo.get("releaseServo");
        DcMotorEx[] driveMotors = {(DcMotorEx) hardwareMap.dcMotor.get("fl"), (DcMotorEx) hardwareMap.dcMotor.get("fr"), (DcMotorEx) hardwareMap.dcMotor.get("bl"), (DcMotorEx) hardwareMap.dcMotor.get("br")};
        driveMotors[0].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[1].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[2].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveMotors[3].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Servo[] handServos = {hardwareMap.servo.get("grabServo"), hardwareMap.servo.get("wristServo")};
        DcMotor[] armMotors = {hardwareMap.dcMotor.get("tiltMotor"), hardwareMap.dcMotor.get("linearMotor")};

        //make the helper classes
        mE = new MecanumEncoders(driveMotors);
        mE.mecanumEncoders(0, 0, 0, false);
        gA = new ArmDriver(armMotors, handServos);
        //MotionController motionController = new MotionController(IMUs, driveMotors);
        telemetry.addData("Status", "initeded");


        telemetry.addLine("Position");
        vs.execute(webcam0);

        while (!isStarted()) {
            telemetry.addData("Position", vs.tStoneX);
            telemetry.update();
        }
        waitForStart();
        Position.position p;
        mE.mecanumEncoders(10, 0, 0, true);
        mE.mecanumEncoders(0, -8, 0, true);
        if(vs.tStoneX < 200) {
            p = LEFT;
            sleep(500);
        } else {
            mE.mecanumEncoders(0, -5, 0, true);
            waitForReady();
            sleep(500);
            if(vs.tStoneX < 200){
                p = CENTER;
            } else{
                p = LEFT;
            }
            mE.mecanumEncoders(0, 5, 0, true );
        }
        mE.mecanumEncoders(3, 0, 0, true);
        mE.mecanumEncoders(0, 3, 0, true);// move to the center of the tile and in a good pos to grab all 3 blocks
        if(p == LEFT){
            mE.mecanumEncoders(0, 0, 30, false);
            gA.tiltArm(500);
            gA.linearArm(150);
            gA.grabberWrist(50/280);
            gA.open();
            gA.tiltArm(-300);
            gA.grab();
            gA.grabberWrist(0);
            gA.tiltArm(200);
            gA.linearArm( 0);
            gA.tiltArm(0);
            mE.mecanumEncoders(0, 0, -30, true);
        } else if(p == RIGHT){ //the Left commands, similar to right, but mirrored
            mE.mecanumEncoders(0, 0, -30, false);
            gA.tiltArm(500);
            gA.linearArm(150);
            gA.grabberWrist(230/280);
            gA.open();
            gA.tiltArm(-300);
            gA.grab();
            gA.grabberWrist(0);
            gA.tiltArm(200);
            gA.linearArm( 0);
            gA.tiltArm(0);
            mE.mecanumEncoders(0, 0, 30, true);
        } else { //if it doesn't find anything it just assumes center and goes for it
            gA.grabberWrist(0);
            gA.tiltArm(500);
            gA.linearArm(150);
            gA.open();
            gA.tiltArm(-300);
            gA.grab();
            gA.grabberWrist(0);
            gA.tiltArm(200);
            gA.linearArm( 0);
            gA.tiltArm(0);
        }

        sleep(10000);
        vs.cancel(true);
        //vn.execute(webcam0);
    }

    private void blockMove(boolean lift) {
        final float targetHeight = 45.0f;
        gA.tiltArm(targetHeight);
        if (lift) {
            gA.open();
            gA.extendArm();
            gA.tiltArm(0);
        }
        if (lift) {
            gA.grab();
        } else {
            gA.open();
            gA.tiltArm(targetHeight);
            gA.retractArm();
        }
    }

    private void waitForReady() {
        while (!mE.ready) {
            sleep(10);
        }
    }
}